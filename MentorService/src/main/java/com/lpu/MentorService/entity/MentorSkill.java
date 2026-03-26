package com.lpu.MentorService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MentorSkill {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private Long mentorId;
	private Long skillId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMentor_id() {
		return mentorId;
	}

	public void setMentor_id(Long mentorId) {
		this.mentorId = mentorId;
	}

	public Long getSkill_id() {
		return skillId;
	}

	public void setSkill_id(Long skillId) {
		this.skillId = skillId;
	}
}
