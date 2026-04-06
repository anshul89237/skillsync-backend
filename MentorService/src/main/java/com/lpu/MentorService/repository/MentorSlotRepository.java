package com.lpu.MentorService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.MentorService.entity.MentorSlot;

@Repository
public interface MentorSlotRepository extends JpaRepository<MentorSlot, Long> {
    List<MentorSlot> findByMentorId(Long mentorId);
}
