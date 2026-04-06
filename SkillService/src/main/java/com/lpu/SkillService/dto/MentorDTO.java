package com.lpu.SkillService.dto;

public class MentorDTO {

	private Long id;
	private Long user_id;
	private String bio;
	private Double experience;
	private Double rating;
	private Double hourly_rate;

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

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Double getExperience() {
		return experience;
	}

	public void setExperience(Double experience) {
		this.experience = experience;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Double getHourly_rate() {
		return hourly_rate;
	}

	public void setHourly_rate(Double hourly_rate) {
		this.hourly_rate = hourly_rate;
	}
}
