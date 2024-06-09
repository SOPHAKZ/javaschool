package com.javaschool.dto.course;

import com.javaschool.enums.RewardType;
import lombok.Data;

import java.util.Set;

@Data
public class RewardDTO {
    private String rewardDescription;
    private String rewardType;
    private Set<Long> freeCourseIds;
    private Double discountPercentage;
}