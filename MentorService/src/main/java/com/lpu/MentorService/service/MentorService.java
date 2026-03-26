package com.lpu.MentorService.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.MentorService.client.SkillServiceClient;
import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.MentorProfileDTO;
import com.lpu.MentorService.dto.SkillDTO;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.exception.MentorNotFoundException;
import com.lpu.MentorService.exception.UserNotFoundException;
import com.lpu.MentorService.repository.MentorRepository;

@Service
public class MentorService {

    @Autowired
    private MentorRepository repository;

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private SkillServiceClient skillClient;

    public Mentor saveMentor(Mentor mentor) 
    {
        return repository.save(mentor);
    }

    public Mentor findMentorById(Long id) 
    {
        return repository.findById(id).orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));
    }

    public List<Mentor> findMentors() 
    {
        return repository.findAll();
    }

    public Mentor updateMentorById(Long id, Mentor mentor) 
    {
        Mentor m = repository.findById(id).orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));
        
        m.setBio(mentor.getBio());
        m.setExperience(mentor.getExperience());
        m.setHourly_rate(mentor.getHourly_rate());
        m.setDob(mentor.getDob());
        m.setLocation(mentor.getLocation());
        m.setPhone(mentor.getPhone());
        
        return repository.save(m);
    }

    public void deleteMentorById(Long id) 
    {
        repository.findById(id).orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + id));
        
        repository.deleteById(id);
    }

    public void updateRating(Long mentorId, Double rating) 
    {
        Mentor mentor = repository.findById(mentorId).orElseThrow(() -> new MentorNotFoundException("Mentor not found with id: " + mentorId));
        
        mentor.setRating(rating);
        
        repository.save(mentor);
    }

    public Mentor findMentorByUserId(Long userId)
    {
        Mentor mentor = repository.findByUser_id(userId);
        
        if (mentor == null) 
        {
            throw new MentorNotFoundException("Mentor not found for user id: " + userId);
        }
        
        return mentor;
    }

    public MentorProfileDTO getMentorProfile(Long userId) 
    {
        Mentor mentor = repository.findByUser_id(userId);
        
        if (mentor == null) 
        {
            throw new MentorNotFoundException("Mentor profile not found for user id: " + userId);
        }

        UsersDTO user = userClient.findUserById(userId);
        
        if (user == null) 
        {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        List<SkillDTO> skills = skillClient.findSkillsByUserId(userId);
        
        int age = 0;

        if (mentor.getDob() != null) 
        {
            age = Period.between(mentor.getDob(), LocalDate.now()).getYears();
        }

        return new MentorProfileDTO(
                mentor.getId(),
                userId,
                user.getName(),
                user.getEmail(),
                mentor.getPhone(),
                mentor.getDob(),
                age,
                mentor.getLocation(),
                mentor.getBio(),
                mentor.getExperience() != null ? mentor.getExperience() : 0.0,
                mentor.getHourly_rate() != null ? mentor.getHourly_rate() : 0.0,
                mentor.getRating() != null ? mentor.getRating() : 0.0,
                skills
        );
    }

    public SkillDTO addSkill(Long userId, SkillDTO skill) 
    {
        skill.setUser_id(userId);
        
        return skillClient.saveSkill(skill);
    }
}