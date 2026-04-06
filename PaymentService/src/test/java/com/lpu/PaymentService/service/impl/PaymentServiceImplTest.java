package com.lpu.PaymentService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.PaymentService.client.MentorServiceClient;
import com.lpu.PaymentService.dto.MentorDTO;
import com.lpu.PaymentService.dto.PaymentRequest;
import com.lpu.PaymentService.dto.SagaPaymentEvent;
import com.lpu.PaymentService.entity.Payment;
import com.lpu.PaymentService.exception.PaymentNotFoundException;
import com.lpu.PaymentService.producer.PaymentEventProducer;
import com.lpu.PaymentService.repository.PaymentRepository;
import com.lpu.java.common_security.config.SecurityUtils;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repository;
    @Mock
    private MentorServiceClient mentorClient;
    @Mock
    private PaymentEventProducer producer;
    @Mock
    private RazorpayClient razorpayClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest paymentRequest;
    private MentorDTO mentorDTO;
    private Payment payment;
    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private MockedStatic<com.razorpay.Utils> mockedUtils;

    @BeforeEach
    void setUp() {
        mockedSecurityUtils = mockStatic(SecurityUtils.class);

        paymentRequest = new PaymentRequest();
        paymentRequest.setMentorId(10L);
        paymentRequest.setSessionId(100L);
        paymentRequest.setHours(2.0);
        paymentRequest.setPaymentMethod("RAZORPAY");

        mentorDTO = new MentorDTO();
        mentorDTO.setId(10L);
        mentorDTO.setHourly_rate(50.0);

        payment = new Payment();
        payment.setId(1L);
        payment.setTotalAmount(100.0);
        payment.setSessionId(100L);
        payment.setLearnerId(1L);
        payment.setStatus("PENDING");
    }

    @AfterEach
    void tearDown() {
        mockedSecurityUtils.close();
        if (mockedUtils != null) {
            mockedUtils.close();
        }
    }

    @Test
    void shouldProcessPayment_Success() throws Exception {
        // given
        mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(1L);
        when(mentorClient.findMentorById(10L)).thenReturn(mentorDTO);
        
        Order mockOrder = mock(Order.class);
        when(mockOrder.get("id")).thenReturn("order_test_123");
        
        razorpayClient.orders = mock(com.razorpay.OrderClient.class);
        when(razorpayClient.orders.create(any(JSONObject.class))).thenReturn(mockOrder);
        
        when(repository.save(any(Payment.class))).thenReturn(payment);

        // when
        Payment result = paymentService.processPayment(paymentRequest);

        // then
        assertNotNull(result);
        verify(repository).save(any(Payment.class));
    }

    @Test
    void shouldVerifyPayment_Success() {
        // given
        Map<String, String> responseData = new HashMap<>();
        responseData.put("razorpay_order_id", "order_123");
        responseData.put("razorpay_payment_id", "pay_123");
        responseData.put("razorpay_signature", "sig_123");

        payment.setRazorpayOrderId("order_123");
        when(repository.findByRazorpayOrderId("order_123")).thenReturn(Optional.of(payment));
        
        mockedUtils = mockStatic(com.razorpay.Utils.class);
        mockedUtils.when(() -> Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                   .thenReturn(true);

        // when
        Payment result = paymentService.verifyRazorpayPayment(responseData);

        // then
        assertEquals("COMPLETED", result.getStatus());
        verify(producer).sendSagaEvent(any(SagaPaymentEvent.class));
    }

    @Test
    void shouldThrowException_whenPaymentNotFound() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PaymentNotFoundException.class, () -> paymentService.findPaymentById(1L));
    }
}
