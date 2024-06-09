package com.javaschool.spec.promotion;

import com.javaschool.enums.PromotionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionFilter {
    private String description;
    private PromotionType promotionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> courseIds;
}