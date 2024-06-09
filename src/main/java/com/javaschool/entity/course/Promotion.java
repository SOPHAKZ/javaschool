package com.javaschool.entity.course;

import com.javaschool.config.audit.UserDateAudit;
import com.javaschool.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Promotion extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    private Integer requiredCourses;
    private Integer freeCourses;
    private LocalDateTime startDate;
    private LocalDateTime endDate;


    @ManyToMany
    @JoinTable(name = "promotions_course",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reward_id", referencedColumnName = "id")
    private Reward reward;
}

