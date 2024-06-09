package com.javaschool.entity.course;

import com.javaschool.config.audit.UserDateAudit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_courses")
@EqualsAndHashCode(callSuper = true)
public class Course extends UserDateAudit {
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
    
    private int duration; // in hours
    
    private LocalDate startDate;

    private LocalDate endDate;
    
    private String studyDays;
    
    private String startHour; // e.g., 8 (for 8 pm)
    
    private String endHour; // e.g., 10 (for 10 pm)

}