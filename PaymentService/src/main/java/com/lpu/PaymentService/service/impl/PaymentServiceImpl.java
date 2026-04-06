package com.lpu.PaymentService.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lpu.PaymentService.client.MentorServiceClient;
import com.lpu.PaymentService.dto.MentorDTO;
import com.lpu.PaymentService.dto.PaymentRequest;
import com.lpu.PaymentService.dto.SagaPaymentEvent;
import com.lpu.PaymentService.entity.Payment;
import com.lpu.PaymentService.exception.PaymentNotFoundException;
import com.lpu.PaymentService.producer.PaymentEventProducer;
import com.lpu.PaymentService.repository.PaymentRepository;
import com.lpu.PaymentService.service.PaymentService;
import com.lpu.java.common_security.config.SecurityUtils;
import com.lpu.java.common_security.dto.ApiResponse;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository repository;
    private final MentorServiceClient mentorClient;
    private final PaymentEventProducer producer;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;

    @Override
        @PreAuthorize("hasAnyRole('LEARNER', 'USER')")
    public Payment processPayment(PaymentRequest request) {
		Long currentUserId = SecurityUtils.getCurrentUserId();
		logger.info("Initializing Razorpay payment for Session ID: {} by User ID: {}", request.getSessionId(), currentUserId);

        request.setLearnerId(currentUserId);

		ApiResponse<MentorDTO> mentorResponse = mentorClient.findMentorById(request.getMentorId());
		MentorDTO mentor = mentorResponse != null ? mentorResponse.getData() : null;
        if (mentor == null) {
            throw new RuntimeException("No mentor found with id: " + request.getMentorId());
        }

        Double totalAmount = request.getHours() * mentor.getHourly_rate();
        
        try {
            // Create Razorpay Order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int)(totalAmount * 100)); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + UUID.randomUUID().toString().substring(0, 8));

            Order order = razorpayClient.orders.create(orderRequest);
            String razorpayOrderId = order.get("id");

            Payment payment = new Payment();
            payment.setLearnerId(request.getLearnerId());
            payment.setMentorId(request.getMentorId());
            payment.setSessionId(request.getSessionId());
            payment.setHours(request.getHours());
            payment.setHourlyRate(mentor.getHourly_rate());
            payment.setTotalAmount(totalAmount);
            payment.setPaymentMethod("RAZORPAY");
            payment.setStatus("PENDING");
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setTransactionId(order.get("receipt"));

            return repository.save(payment);

        } catch (Exception e) {
            logger.error("Failed to create Razorpay order: {}", e.getMessage());
            throw new RuntimeException("Payment initialization failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Payment verifyRazorpayPayment(Map<String, String> responseData) {
        String orderId = responseData.get("razorpay_order_id");
        String paymentId = responseData.get("razorpay_payment_id");
        String signature = responseData.get("razorpay_signature");

        logger.info("Verifying Razorpay payment for Order ID: {}", orderId);

        try {
            // Verify Signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            boolean isValid = Utils.verifyPaymentSignature(options, razorpaySecret);

            Payment payment = repository.findByRazorpayOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment record not found for order: " + orderId));

            if (isValid) {
                payment.setStatus("SUCCESS");
                payment.setRazorpayPaymentId(paymentId);
                payment.setRazorpaySignature(signature);
                payment.setPaidAt(LocalDateTime.now());
                
                Payment savedPayment = repository.save(payment);
                
                // Dispatch Saga Event for SUCCESS
                SagaPaymentEvent sagaEvent = new SagaPaymentEvent(
                        savedPayment.getId(),
                        savedPayment.getSessionId(),
                        "SUCCESS"
                );
                producer.sendSagaEvent(sagaEvent);
                logger.info("Payment verified and Saga triggered for Session ID: {}", savedPayment.getSessionId());
                
                return savedPayment;
            } else {
                payment.setStatus("FAILED");
                repository.save(payment);
                throw new RuntimeException("Invalid payment signature");
            }

        } catch (Exception e) {
            logger.error("Payment verification failed: {}", e.getMessage());
            throw new RuntimeException("Verification failed: " + e.getMessage());
        }
    }

    @Override
	@Cacheable(value = "payments", key = "#id", unless = "#result == null")
	public Payment findPaymentById(Long id) {
		logger.info("Fetching payment details by ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
					logger.error("Payment lookup failed: ID {} not found", id);
					return new PaymentNotFoundException("Payment not found with id: " + id);
				});
	}

    @Override
    @PreAuthorize("hasAnyRole('LEARNER', 'USER', 'ADMIN')")
	@Cacheable(value = "paymentsByLearner", key = "#learnerId", unless = "#result == null")
	public List<Payment> findPaymentsByLearnerId(Long learnerId) {
		logger.info("Fetching payments for Learner ID: {}", learnerId);
        List<Payment> payments = repository.findByLearnerId(learnerId);
		logger.info("Found {} payments for Learner ID: {}", payments.size(), learnerId);
		return payments;
	}

    @Override
    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN')")
	@Cacheable(value = "paymentsByMentor", key = "#mentorId", unless = "#result == null")
	public List<Payment> findPaymentsByMentorId(Long mentorId) {
		logger.info("Fetching payments for Mentor ID: {}", mentorId);
        List<Payment> payments = repository.findByMentorId(mentorId);
		logger.info("Found {} payments for Mentor ID: {}", payments.size(), mentorId);
		return payments;
	}

    @Override
    @PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "allPayments", unless = "#result == null || #result.isEmpty()")
	public List<Payment> findAllPayments() {
		logger.info("Fetching all payments in the system");
        List<Payment> payments = repository.findAll();
		logger.info("Found {} total payment records", payments.size());
		return payments;
	}
}
