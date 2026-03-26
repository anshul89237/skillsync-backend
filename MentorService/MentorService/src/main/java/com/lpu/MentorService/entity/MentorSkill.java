package com.lpu.MentorService.entity;

import jakarta.persistence.Entity;

@Entity
public class MentorSkill {

	private Long mentor_id;
	private Long skill_id;

	public Long getMentor_id() {
		return mentor_id;
	}

	public void setMentor_id(Long mentor_id) {
		this.mentor_id = mentor_id;
	}

	public Long getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Long skill_id) {
		this.skill_id = skill_id;
	}
}
