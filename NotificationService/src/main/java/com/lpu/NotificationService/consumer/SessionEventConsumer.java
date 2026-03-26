package com.lpu.NotificationService.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lpu.NotificationService.client.MentorServiceClient;
import com.lpu.NotificationService.client.UserServiceClient;
import com.lpu.NotificationService.dto.MentorDTO;
import com.lpu.NotificationService.dto.SessionEvent;
import com.lpu.NotificationService.dto.UsersDTO;
import com.lpu.NotificationService.service.EmailService;

@Component
public class SessionEventConsumer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserServiceClient userClient;
    
    @Autowired
    private MentorServiceClient mentorClient;

    @RabbitListener(queues = "session.queue")
    public void consume(SessionEvent event) {

        UsersDTO user = userClient.findUserById(event.getLearnerId());
        
        MentorDTO mentor = mentorClient.findMentorById(event.getMentorId());
        UsersDTO mentorr = userClient.findUserById(mentor.getUser_id());
 

        String subject = "Session Update";
        String message = "Greetings from SkillSync Academy! \n\nDear, " 
        		+ user.getName() +
                ". \nYour session with mentor "
                + mentorr.getName()
                + " is " + event.getStatus()
                + ", which is on " + event.getSession_date() + ".";

        emailService.sendEmail(user.getEmail(), subject, message);
    }
}
