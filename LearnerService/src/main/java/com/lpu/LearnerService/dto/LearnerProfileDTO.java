package com.lpu.LearnerService.dto;

import java.time.LocalDate;
import java.util.List;

public class LearnerProfileDTO {

    private Long id;
    private Long userId;

    private String name;     // from User Service
    private String email;    // from User Service

    private String phone;
    private String profilePic;
    private LocalDate dob;
    private int age; 

    private String location;
    private String bio;

    private List<SkillDTO> skills;

    public LearnerProfileDTO(Long id, Long userId, String name, String email,
                             String phone, String profilePic, LocalDate dob, int age,
                             String location, String bio, List<SkillDTO> skills)
    {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePic = profilePic;
        this.dob = dob;
        this.age = age;
        this.location = location;
        this.bio = bio;
        this.skills = skills;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
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

	public List<SkillDTO> getSkills() {
		return skills;
	}

	public void setSkills(List<SkillDTO> skills) {
		this.skills = skills;
	}
}