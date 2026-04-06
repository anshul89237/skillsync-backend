package com.lpu.MentorService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lpu.MentorService.entity.MentorSlot;
import com.lpu.MentorService.repository.MentorSlotRepository;

@RestController
@RequestMapping("/mentor-slots")
public class MentorSlotController {

    @Autowired
    private MentorSlotRepository repository;

    @PostMapping
    public MentorSlot createSlot(@RequestBody MentorSlot slot) {
        if (slot.getMaxLearners() == null) {
            slot.setMaxLearners(100);
        }
        slot.setCurrentLearners(0);
        return repository.save(slot);
    }

    @GetMapping("/{id}")
    public MentorSlot getSlot(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Slot not found"));
    }

    @GetMapping("/mentor/{mentorId}")
    public List<MentorSlot> getSlotsByMentor(@PathVariable Long mentorId) {
        return repository.findByMentorId(mentorId);
    }

    @PutMapping("/{id}/increment")
    public MentorSlot incrementSlot(@PathVariable Long id) {
        MentorSlot slot = repository.findById(id).orElseThrow(() -> new RuntimeException("Slot not found"));
        if (slot.getCurrentLearners() >= slot.getMaxLearners()) {
            throw new RuntimeException("Slot is full! Limit of " + slot.getMaxLearners() + " learners reached.");
        }
        slot.setCurrentLearners(slot.getCurrentLearners() + 1);
        return repository.save(slot);
    }
    
    @DeleteMapping("/{id}")
    public void deleteSlot(@PathVariable Long id) {
    	repository.deleteById(id);
    }
}
