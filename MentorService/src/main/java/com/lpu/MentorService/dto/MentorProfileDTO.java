package com.lpu.MentorService.dto;

import java.time.LocalDate;
import java.util.List;

public class MentorProfileDTO {

    private Long mentorId;
    private Long userId;

    private String name;     // from User Service
    private String email;    // from User Service

    private String phone;
    private LocalDate dob;
    private int age;

    private String location;

    private String bio;
    private double experience;
    private double hourlyRate;
    private double rating;

    private List<SkillDTO> skills;

    // Constructor
    public MentorProfileDTO(Long mentorId, Long userId,
                            String name, String email,
                            String phone, 
                            LocalDate dob, int age,
                            String location,
                            String bio, double experience,
                            double hourlyRate, double rating,
                            List<SkillDTO> skills) {

        this.mentorId = mentorId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.age = age;
        this.location = location;
        this.bio = bio;
        this.experience = experience;
        this.hourlyRate = hourlyRate;
        this.rating = rating;
        this.skills = skills;
    }

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public List<SkillDTO> getSkills() {
		return skills;
	}

	public void setSkills(List<SkillDTO> skills) {
		this.skills = skills;
	}

    // Getters & Setters
    
}
