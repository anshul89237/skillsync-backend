package com.lpu.NotificationService.consumer;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
 
import com.lpu.NotificationService.client.MentorServiceClient;
import com.lpu.NotificationService.client.UserServiceClient;
import com.lpu.NotificationService.dto.MentorDTO;
import com.lpu.NotificationService.dto.SessionEvent;
import com.lpu.NotificationService.dto.UsersDTO;
import com.lpu.NotificationService.service.EmailService;
 
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(SessionEventConsumer.class);

    private final EmailService emailService;
    private final UserServiceClient userClient;
    private final MentorServiceClient mentorClient;
 
    @RabbitListener(queues = "session.queue")
    public void consume(SessionEvent event) {
		logger.info("Received SessionEvent from RabbitMQ: Session ID: {}, Status: {}", event.getSessionId(), event.getStatus());
        try {
            UsersDTO student = userClient.findUserById(event.getUserId());
            MentorDTO mentor = mentorClient.findMentorById(event.getMentorId());
            UsersDTO mentorUser = userClient.findUserById(mentor.getUser_id());
    
            String status = event.getStatus();
            String subject, message;

            switch (status) {
                case "PENDING_APPROVAL":
                    // To Mentor
                    subject = "New Session Booking Request";
                    message = String.format("Dear Mentor, \n\nA new session has been booked by student (ID: %d, Name: %s). \nPlease kindly approve, reject, or cancel the session in your dashboard.", 
                        event.getUserId(), student.getName());
                    emailService.sendEmail(mentorUser.getEmail(), subject, message);
                    break;

                case "APPROVED":
                    // To Student
                    subject = "Session Approved";
                    message = String.format("Dear %s, \n\nYour session with mentor %s has been APPROVED. \nTimings: %s. \n\nHappy Learning!", 
                        student.getName(), mentorUser.getName(), event.getSession_date());
                    emailService.sendEmail(student.getEmail(), subject, message);

                    // To Mentor
                    subject = "Session Scheduled Successfully";
                    message = String.format("Dear %s, \n\nYour session is scheduled with student (ID: %d, Name: %s). \nTimings: %s.", 
                        mentorUser.getName(), event.getUserId(), student.getName(), event.getSession_date());
                    emailService.sendEmail(mentorUser.getEmail(), subject, message);
                    break;

                case "REJECTED":
                case "CANCELLED":
                    // To Student
                    subject = "Session " + status;
                    message = String.format("Dear %s, \n\nYour session with mentor %s has been %s by the mentor. \nSession Timings: %s.", 
                        student.getName(), mentorUser.getName(), status, event.getSession_date());
                    emailService.sendEmail(student.getEmail(), subject, message);
                    break;

                case "PAYMENT_PENDING":
                    // To Student
                    subject = "Action Required: Pay to Confirm Session";
                    message = String.format("Dear %s, \n\nPlease pay the booking amount to confirm your session with mentor %s (Mentor ID: %d). \nAfter successful payment, the session will be officially scheduled.", 
                        student.getName(), mentorUser.getName(), event.getMentorId());
                    emailService.sendEmail(student.getEmail(), subject, message);
                    break;

                case "SCHEDULED":
                    // To Student
                    subject = "Session Scheduled - SkillSync Academy";
                    message = String.format("Dear %s, \n\nYour session with mentor %s has been officially scheduled. \nTimings: %s. \n\nPlease check your dashboard for the meeting link.", 
                        student.getName(), mentorUser.getName(), event.getSession_date());
                    emailService.sendEmail(student.getEmail(), subject, message);

                    // To Mentor
                    subject = "Class Scheduled Details - SkillSync Academy";
                    message = String.format("Dear %s, \n\nA new class is scheduled with student (ID: %d, Name: %s). \nTimings: %s. \nNumber of students present: 1", 
                        mentorUser.getName(), event.getUserId(), student.getName(), event.getSession_date());
                    emailService.sendEmail(mentorUser.getEmail(), subject, message);
                    break;

                default:
                    logger.warn("No specific notification logic implemented for status: {}", status);
            }
			
        } catch (Exception e) {
            logger.error("Failed to process Session Notification message for session ID {}: {}", event.getSessionId(), e.getMessage());
        }
    }
}
