package com.javaschool.dto.course;

import com.javaschool.enums.PromotionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionDTO {
    private Long id;
    private String description;
    private PromotionType promotionType;
    private Integer requiredCourses;
    private Integer freeCourses;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> courseIds;

    private RewardDTO rewardId;

}
