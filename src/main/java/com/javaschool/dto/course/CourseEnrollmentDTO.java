package com.javaschool.dto.course;

import lombok.Data;

import java.util.List;

@Data
public class CourseEnrollmentDTO {
    private List<Long> courseIds;
    private Long enrollmentId;
    private Boolean isCompleted;
}
