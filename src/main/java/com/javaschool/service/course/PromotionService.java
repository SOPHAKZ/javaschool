package com.javaschool.service.course;

import com.javaschool.dto.course.PromotionDTO;

import com.javaschool.entity.course.Promotion;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PromotionService {
    Promotion createPromotion(PromotionDTO promotionDTO);
    Promotion getPromotionById(Long id);
    Page<Promotion> getAllPromotions(Map<String,String> page);
    Page<Promotion> specificationSearch(Map<String,String> param);
}
