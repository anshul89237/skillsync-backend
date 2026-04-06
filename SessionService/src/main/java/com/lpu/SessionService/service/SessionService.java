package com.lpu.SessionService.service;

import java.util.List;
import com.lpu.SessionService.dto.SessionDTO;

public interface SessionService {

    SessionDTO createSession(SessionDTO sessionDTO);

    SessionDTO updateSessionDateById(Long id, SessionDTO sessionDTO);

    void deleteSessionById(Long id);

    SessionDTO updateStatus(Long id, String status);

    SessionDTO findSessionById(Long id);

    List<SessionDTO> findAllSessions();

    List<SessionDTO> findSessionsByLearnerId(Long userId);

    List<SessionDTO> findSessionsByMentorId(Long mentorId);

    List<SessionDTO> findSessionsByLearnerAndMentor(Long userId, Long mentorId);
}
