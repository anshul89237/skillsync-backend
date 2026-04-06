package com.lpu.PaymentService.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.PaymentService.dto.PaymentRequest;
import com.lpu.PaymentService.entity.Payment;
import com.lpu.PaymentService.service.PaymentService;
import com.lpu.java.common_security.config.JwtUtil;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldProcessPayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setSessionId(100L);
        
        Payment result = new Payment();
        result.setId(1L);
        
        when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(result);

        mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnPaymentById() throws Exception {
        Payment result = new Payment();
        result.setId(1L);
        when(paymentService.findPaymentById(1L)).thenReturn(result);

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnPaymentsByLearnerId() throws Exception {
        when(paymentService.findPaymentsByLearnerId(1L)).thenReturn(Arrays.asList(new Payment()));

        mockMvc.perform(get("/payments/learner/1"))
                .andExpect(status().isOk());
    }
}
