package com.lpu.MentorService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.repository.MentorRepository;

@Service
public class MentorService {

	@Autowired
	private MentorRepository repository;
	
	public Mentor saveMentor(Mentor mentor) {
		return repository.save(mentor);
	}
	
	public Mentor findMentorById(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public List<Mentor> findMentors() {
		return repository.findAll();
	}
	
	public Mentor updateMentorById(Long id, Mentor mentor) {
		Mentor m = repository.findById(id).orElse(null);
		
		m.setBio(mentor.getBio());
		m.setExperience(mentor.getExperience());
		m.setHourly_rate(mentor.getHourly_rate());
		
		return repository.save(m);
	}
	
	public void deleteMentorById(Long id) {
		repository.deleteById(id);
	}
	
	 public void updateRating(Long mentorId, Double rating) {

	        Mentor mentor = repository.findById(mentorId)
	                .orElseThrow(() -> new RuntimeException("Mentor not found"));

	        mentor.setRating(rating);

	        repository.save(mentor);
	    }
}
