package com.javaschool.spec.Course;

import lombok.Data;

@Data
public class CourseFilter {
    private Long id;
    private String name;
    private Boolean isOnline;
    private Long categoryId;
}
