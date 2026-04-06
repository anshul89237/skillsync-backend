package com.lpu.SessionService.mapper;

import com.lpu.SessionService.dto.SessionDTO;
import com.lpu.SessionService.entity.Session;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionMapper {
    public SessionDTO toDto(Session session) {
        if (session == null) {
            return null;
        }

        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setMentorId(session.getMentorId());
        dto.setUserId(session.getUserId());
        dto.setLearnerId(session.getUserId());
        dto.setSlotId(session.getSlotId());
        dto.setSessionDate(session.getSessionDate());
        dto.setStartTime(session.getSessionDate());
        dto.setEndTime(session.getSessionDate());
        dto.setStatus(session.getStatus());
        dto.setCreatedAt(session.getCreatedAt());
        return dto;
    }

    public Session toEntity(SessionDTO sessionDTO) {
        if (sessionDTO == null) {
            return null;
        }

        Session session = new Session();
        session.setId(sessionDTO.getId());
        session.setMentorId(sessionDTO.getMentorId());
        session.setUserId(sessionDTO.getUserId() != null ? sessionDTO.getUserId() : sessionDTO.getLearnerId());
        session.setSlotId(sessionDTO.getSlotId());
        session.setSessionDate(sessionDTO.getSessionDate() != null ? sessionDTO.getSessionDate() : sessionDTO.getStartTime());
        session.setStatus(sessionDTO.getStatus());
        return session;
    }

    public List<SessionDTO> toDtoList(List<Session> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return Collections.emptyList();
        }
        return sessions.stream().map(this::toDto).collect(Collectors.toList());
    }
}
