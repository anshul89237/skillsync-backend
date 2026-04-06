package com.lpu.MentorService.dto;

import java.io.Serializable;

public class SkillDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	@com.fasterxml.jackson.annotation.JsonProperty("userId")
	private Long user_id;
	private String name;
	private String category;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
