package com.lpu.SessionService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.SessionService.client.MentorServiceClient;
import com.lpu.SessionService.client.UserServiceClient;
import com.lpu.SessionService.dto.MentorDTO;
import com.lpu.SessionService.dto.UsersDTO;
import com.lpu.SessionService.entity.Session;
import com.lpu.SessionService.service.SessionService;

@RequestMapping("/sessions")
@RestController
public class SessionController {

	@Autowired
	private SessionService service;
	
	@Autowired
	private UserServiceClient userClient;
	
	@Autowired
	private MentorServiceClient mentorClient;
	
	@PostMapping
	public Session createsSession(@RequestBody Session session) {
		
		UsersDTO user = userClient.findUserById(session.getLearnerId());
		MentorDTO mentor = mentorClient.findMentorById(session.getMentorId());

		if (user == null) {
			throw new RuntimeException("No learner found with learner id : " + session.getLearnerId() + ". Create user account");
		}
		
		if (mentor == null) {
			throw new RuntimeException("No mentor found with mentor id : " + session.getMentorId() + ". Create mentor account");
		}
		
		session.setCreated_at(LocalDateTime.now());
		
		return service.createSession(session);
	}
	
	@GetMapping("/{id}")
	public Session findSessionById(@PathVariable("id") Long id) {
		return service.findSessionById(id);
	}
	
	@GetMapping
	public List<Session> findAllSessions() {
		return service.findAllSessions();
	}
	
	@PutMapping("/{id}/accept")
	public Session acceptSessionById(@PathVariable("id") Long id) {
		return service.acceptSessionById(id);
	}
	
	@PutMapping("/{id}/reject")
	public Session rejectSessionById(@PathVariable("id") Long id) {
		return service.rejectSessionById(id);
	}
	
	@PutMapping("/{id}/cancel")
	public Session cancelSessionById(@PathVariable("id") Long id) {
		return service.cancelSessionById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSessionById(@PathVariable("id") Long id) {
		service.deleteSessionById(id);
		System.out.println("Session deleted");
	}
	
	@PutMapping("/{id}/{status}")
    public ResponseEntity<Session> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
	
}
