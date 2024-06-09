package com.javaschool.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class EnrollmentRequestDTO {
    private Long userId;
    private List<Long> coursesId;
    private Long promotionId;
}
