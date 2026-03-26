package com.lpu.MentorService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lpu.MentorService.client.UserServiceClient;
import com.lpu.MentorService.dto.UsersDTO;
import com.lpu.MentorService.entity.Mentor;
import com.lpu.MentorService.service.MentorService;

@RequestMapping("/mentors")
@RestController
public class MentorController {

	@Autowired
	private MentorService service;

	@Autowired
	private UserServiceClient client;

	@PostMapping("/apply")
	public Mentor saveMentor(@RequestBody Mentor mentor) {

		UsersDTO user = client.findUserById(mentor.getUser_id());

		if (user == null) {
			throw new RuntimeException("No user found with user id : " + mentor.getUser_id() + ". Create account");
		}

		return service.saveMentor(mentor);
	}

	@GetMapping("/{id}")
	public Mentor findMentorById(@PathVariable("id") Long id) {
		return service.findMentorById(id);
	}

	@GetMapping
	public List<Mentor> findAllMentors() {
		return service.findMentors();
	}

	@PutMapping("/{id}")
	public Mentor updateMentorById(@PathVariable("id") Long id, @RequestBody Mentor mentor) {

		UsersDTO u = client.findUserById(mentor.getUser_id());

		if (u == null) {
			throw new RuntimeException("No user found with user id : " + mentor.getUser_id() + ". Create account");
		}

		return service.updateMentorById(id, mentor);
	}

	@PutMapping("/users/{user_id}")
	public UsersDTO updateUserDTOById(@PathVariable("user_id") Long user_id, @RequestBody UsersDTO user) {

		UsersDTO u = client.findUserById(user_id);

		if (u == null) {
			throw new RuntimeException("No user found with user id : " + user.getId() + ". Create account");
		}

		return client.updateUserById(user_id, user);
	}

	// mentor availability

	@DeleteMapping("/{id}")
	public void deleteMentorById(@PathVariable("id") Long id) {
		service.deleteMentorById(id);
	}
	
	@PutMapping("/{id}/rating")
    public String updateRating(@PathVariable Long id,
                               @RequestParam Double rating) {

        service.updateRating(id, rating);
        return "Rating updated";
    }
}
