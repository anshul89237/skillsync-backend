package com.lpu.ReviewService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long mentorId;
    private Long user_id;
    private Double rating;
    private String comment;
    private LocalDateTime created_at;
}
