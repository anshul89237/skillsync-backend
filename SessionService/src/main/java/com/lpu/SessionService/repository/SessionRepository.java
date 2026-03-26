package com.lpu.SessionService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.SessionService.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

	List<Session> findByLearnerId(Long learnerId);

    List<Session> findByMentorId(Long mentorId);
    
    List<Session> findByStatus(String status);
    
}
