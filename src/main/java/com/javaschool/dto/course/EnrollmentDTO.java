package com.javaschool.dto.course;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EnrollmentDTO {
    private Long id;
    private List<Long> courseIds;
    private Long userId;
    private Long promotionId;
    private Boolean isCompleted;
    private Double paymentPrice;
    private LocalDateTime enrollmentDate;

}
