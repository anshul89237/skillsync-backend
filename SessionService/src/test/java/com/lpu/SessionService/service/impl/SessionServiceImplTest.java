package com.lpu.SessionService.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lpu.SessionService.client.MentorServiceClient;
import com.lpu.SessionService.client.MentorSlotServiceClient;
import com.lpu.SessionService.dto.MentorDTO;
import com.lpu.SessionService.dto.MentorSlotDTO;
import com.lpu.SessionService.dto.SessionDTO;
import com.lpu.SessionService.dto.SessionEvent;
import com.lpu.SessionService.entity.Session;
import com.lpu.SessionService.exception.SessionNotFoundException;
import com.lpu.SessionService.mapper.SessionMapper;
import com.lpu.SessionService.producer.SessionEventProducer;
import com.lpu.SessionService.repository.SessionRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private SessionRepository repository;
    @Mock
    private SessionEventProducer producer;
    @Mock
    private MentorServiceClient mentorClient;
    @Mock
    private MentorSlotServiceClient mentorSlotClient;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private Session session;
    private SessionDTO sessionDTO;
    private MentorDTO mentorDTO;

    @BeforeEach
    void setUp() {
        session = new Session();
        session.setId(1L);
        session.setUserId(1L);
        session.setMentorId(10L);
        session.setSlotId(100L);
        session.setStatus("PENDING");

        sessionDTO = new SessionDTO();
        sessionDTO.setId(1L);
        sessionDTO.setUserId(1L);
        sessionDTO.setMentorId(10L);
        sessionDTO.setSlotId(100L);
        sessionDTO.setStartTime(LocalDateTime.now());

        mentorDTO = new MentorDTO();
        mentorDTO.setId(10L);
        
        lenient().when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
    }

    @Test
    void shouldCreateSession_whenFirstSession() {
        // given
        when(mentorClient.findMentorById(anyLong())).thenReturn(mentorDTO);
        when(repository.findByUserIdAndMentorId(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(sessionMapper.toEntity(any(SessionDTO.class))).thenReturn(session);
        when(repository.save(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDTO);
        
        MentorSlotDTO slot = new MentorSlotDTO();
        slot.setStartTime(LocalDateTime.now());
        when(mentorSlotClient.getSlot(anyLong())).thenReturn(slot);

        // when
        SessionDTO result = sessionService.createSession(sessionDTO);

        // then
        assertNotNull(result);
        assertEquals("PENDING_APPROVAL", session.getStatus());
        verify(repository).save(session);
        verify(mentorSlotClient).incrementSlot(100L);
    }

    @Test
    void shouldCreateSession_whenSubsequentSession() {
        // given
        when(mentorClient.findMentorById(anyLong())).thenReturn(mentorDTO);
        when(repository.findByUserIdAndMentorId(anyLong(), anyLong())).thenReturn(List.of(new Session()));
        when(sessionMapper.toEntity(any(SessionDTO.class))).thenReturn(session);
        when(repository.save(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDTO);
        
        MentorSlotDTO slot = new MentorSlotDTO();
        slot.setStartTime(LocalDateTime.now());
        lenient().when(mentorSlotClient.getSlot(anyLong())).thenReturn(slot);

        // when
        SessionDTO result = sessionService.createSession(sessionDTO);

        // then
        assertNotNull(result);
        assertEquals("PAYMENT_PENDING", session.getStatus());
    }

    @Test
    void shouldThrowException_whenMentorNotFound() {
        // given
        when(mentorClient.findMentorById(anyLong())).thenReturn(null);

        // when & then
        assertThrows(RuntimeException.class, () -> sessionService.createSession(sessionDTO));
    }

    @Test
    void shouldUpdateStatus_andProduceEvent() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDTO);

        // when
        SessionDTO result = sessionService.updateStatus(1L, "APPROVED");

        // then
        assertNotNull(result);
        assertEquals("APPROVED", session.getStatus());
        verify(producer).sendSessionEvent(any(SessionEvent.class));
    }

    @Test
    void shouldThrowException_whenSessionNotFound() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(SessionNotFoundException.class, () -> sessionService.findSessionById(1L));
    }
}
