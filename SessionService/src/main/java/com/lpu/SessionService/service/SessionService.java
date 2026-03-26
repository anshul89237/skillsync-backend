package com.lpu.SessionService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.SessionService.dto.SessionEvent;
import com.lpu.SessionService.entity.Session;
import com.lpu.SessionService.exception.SessionNotFoundException;
import com.lpu.SessionService.producer.SessionEventProducer;
import com.lpu.SessionService.repository.SessionRepository;

@Service
public class SessionService 
{

	@Autowired
	private SessionRepository repository;

	@Autowired
	private SessionEventProducer producer;

	public Session createSession(Session session) {
		return repository.save(session);
	}

	public Session findSessionById(Long id) 
	{
		return repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
	}

	public List<Session> findAllSessions() 
	{
		return repository.findAll();
	}

	public Session acceptSessionById(Long id) 
	{
		Session session = repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		session.setStatus("ACCEPTED");
		
		return repository.save(session);
	}

	public Session rejectSessionById(Long id) 
	{
		Session session = repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		session.setStatus("REJECTED");
		
		return repository.save(session);
	}

	public Session cancelSessionById(Long id) 
	{
		Session session = repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		session.setStatus("CANCELLED");
		
		return repository.save(session);
	}

	public Session updateSessionDateById(Long id, Session session) 
	{
		Session s = repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		s.setSession_date(session.getSession_date());
		
		return repository.save(s);
	}

	public void deleteSessionById(Long id) 
	{
		repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		repository.deleteById(id);
	}

	public Session updateStatus(Long id, String status) 
	{
		Session session = repository.findById(id).orElseThrow(() -> new SessionNotFoundException("Session not found with id: " + id));
		
		session.setStatus(status);
		
		repository.save(session);

		SessionEvent event = new SessionEvent(session.getId(), session.getLearnerId(), session.getMentorId(), session.getStatus(), session.getSession_date());
		
		producer.sendSessionEvent(event);

		return session;
	}
}