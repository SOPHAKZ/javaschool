package com.javaschool.spec.enrollment;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class EnrollmentFilter {
    private Long id;
    private Long userId;
    private Long promotionId;
    private Boolean isCompleted;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
