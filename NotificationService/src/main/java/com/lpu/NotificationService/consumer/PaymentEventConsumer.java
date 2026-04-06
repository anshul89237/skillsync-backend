package com.lpu.NotificationService.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.lpu.NotificationService.client.MentorServiceClient;
import com.lpu.NotificationService.client.UserServiceClient;
import com.lpu.NotificationService.dto.MentorDTO;
import com.lpu.NotificationService.dto.PaymentEvent;
import com.lpu.NotificationService.dto.UsersDTO;
import com.lpu.NotificationService.service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(PaymentEventConsumer.class);

	private final EmailService emailService;
	private final UserServiceClient userClient;
	private final MentorServiceClient mentorClient;

	@RabbitListener(queues = "payment.queue")
	public void consume(PaymentEvent event) {
		logger.info("Received PaymentEvent from RabbitMQ: Payment ID: {}, Status: {}", event.getPaymentId(),
				event.getStatus());

		try {
			UsersDTO user = userClient.findUserById(event.getLearnerId());
			MentorDTO mentor = mentorClient.findMentorById(event.getMentorId());
			UsersDTO mentorUser = userClient.findUserById(mentor.getUser_id());

			String status = event.getStatus();
			String subject, message;

			if ("SUCCESS".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
				// To Student: Payment Receipt
				subject = "Payment Receipt - SkillSync Academy";
				message = String.format("Dear %s, \n\nYour payment of Rs.%.2f for mentor %s has been successful. \n\n" +
						"Payment Details: \n" +
						"Transaction ID: %s \n" +
						"Amount Paid: Rs.%.2f \n" +
						"Paid On: %s \n\n" +
						"Your session is being scheduled. You will receive another mail shortly with the class details.",
						user.getName(), event.getTotalAmount(), mentorUser.getName(), 
						event.getTransactionId(), event.getTotalAmount(), event.getPaidAt());
				
				logger.info("Sending Payment Receipt to: {}", user.getEmail());
				emailService.sendEmail(user.getEmail(), subject, message);
			} else {
				// Handle FAILED or other status
				subject = "Payment " + status;
				message = String.format("Dear %s, \n\nYour payment of Rs.%.2f for mentor %s is %s.",
						user.getName(), event.getTotalAmount(), mentorUser.getName(), status);
				emailService.sendEmail(user.getEmail(), subject, message);
			}

		} catch (Exception e) {
			logger.error("Error processing PaymentEvent for payment ID {}: {}", event.getPaymentId(), e.getMessage());
		}
	}
}
