package com.javaschool.dto.course;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseDTO {
    private long id;
    private String name;
    private String description;
    private String youtubeLink;
    private String onlineLink;
    private boolean isOnline;
    private long categoryId;
    private double price;
    private int duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private String studyDays;
    private String startHour;
    private String endHour;

}
