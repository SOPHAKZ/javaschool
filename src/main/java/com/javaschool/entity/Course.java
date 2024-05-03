package com.javaschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;

    private String youtubeLink;

    private String onlineLink;

    private boolean isOnline;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(nullable = false)
    private double price;
    
    @Column(nullable = false)
    private int duration; // in hours
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private String studyDays;
    
    @Column(nullable = false)
    private int startHour; // e.g., 8 (for 8 pm)
    
    @Column(nullable = false)
    private int endHour; // e.g., 10 (for 10 pm)

}