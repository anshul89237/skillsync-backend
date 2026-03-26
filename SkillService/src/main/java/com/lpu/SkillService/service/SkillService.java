package com.lpu.SkillService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lpu.SkillService.client.MentorServiceClient;
import com.lpu.SkillService.client.MentorSkillServiceClient;
import com.lpu.SkillService.client.UserServiceClient;
import com.lpu.SkillService.dto.MentorDTO;
import com.lpu.SkillService.dto.MentorSkillDTO;
import com.lpu.SkillService.dto.UsersDTO;
import com.lpu.SkillService.entity.Skill;
import com.lpu.SkillService.exception.SkillNotFoundException;
import com.lpu.SkillService.exception.UserNotFoundException;
import com.lpu.SkillService.repository.SkillRepository;

@Service
public class SkillService 
{

	@Autowired
	private SkillRepository repository;

	@Autowired
	private UserServiceClient userClient;

	@Autowired
	private MentorServiceClient mentorClient;

	@Autowired
	private MentorSkillServiceClient mentorSkillClient;

	public Skill saveSkill(Skill skill) 
	{
		Skill savedSkill = repository.save(skill);

		UsersDTO user = userClient.findUserById(skill.getUserId());
		
		if (user == null) 
		{
			throw new UserNotFoundException("User not found with id: " + skill.getUserId());
		}

		if ("ROLE_MENTOR".equals(user.getRole())) 
		{
			MentorDTO mentor = mentorClient.findMentorByUserId(skill.getUserId());
			
			if (mentor == null) 
			{
				throw new RuntimeException("Mentor not found for user id: " + skill.getUserId());
			}
			
			MentorSkillDTO ms = new MentorSkillDTO();
			
			ms.setMentorId(mentor.getId());
			ms.setSkillId(savedSkill.getId());
			
			mentorSkillClient.addSkillToMentor(ms);
		}

		return savedSkill;
	}

	public Skill findSkillById(Long id)
	{
		return repository.findById(id).orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + id));
	}

	public List<Skill> findAllSkills() 
	{
		return repository.findAll();
	}

	public Skill updateSkillById(Long id, Skill skill) 
	{
		Skill s = repository.findById(id).orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + id));
		
		s.setName(skill.getName());
		s.setCategory(skill.getCategory());
		
		return repository.save(s);
	}

	public void deleteSkillById(Long id) 
	{
		repository.findById(id).orElseThrow(() -> new SkillNotFoundException("Skill not found with id: " + id));
		
		repository.deleteById(id);
	}

	public List<Skill> findSkillByUserId(Long userId) 
	{
		return repository.findByUserId(userId);
	}
}