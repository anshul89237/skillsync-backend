package com.lpu.PaymentService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lpu.PaymentService.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByLearnerId(Long learnerId);
    List<Payment> findByMentorId(Long mentorId);
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
