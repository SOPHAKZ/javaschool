package com.javaschool.entity.course;

import com.javaschool.enums.RewardType;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rewardDescription;
//    @Enumerated(EnumType.STRING)
    private String rewardType;

    @ElementCollection
    private Set<Long> freeCourseIds;

    private Double discountPercentage;
}
