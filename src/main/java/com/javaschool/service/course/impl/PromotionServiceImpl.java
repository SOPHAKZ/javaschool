package com.javaschool.service.course.impl;

import com.javaschool.dto.course.PromotionDTO;
import com.javaschool.entity.course.Promotion;
import com.javaschool.enums.PromotionType;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.PromotionMapper;
import com.javaschool.repository.PromotionRepository;
import com.javaschool.service.course.PromotionService;
import com.javaschool.spec.promotion.PromotionFilter;
import com.javaschool.spec.promotion.PromotionSpecification;
import com.javaschool.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;


    @Override
    public Promotion createPromotion(PromotionDTO promotionDTO) {
        return promotionRepository.save(promotionMapper.promotion(promotionDTO));
    }

    @Override
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion","id",id.toString()));
    }

    @Override
    public Page<Promotion> getAllPromotions(Map<String, String> page) {
        Pageable pageable = PageUtil.getPageable(page);

        return promotionRepository.findAll(pageable);
    }

    @Override
    public Page<Promotion> specificationSearch(Map<String, String> param) {

        PromotionFilter filter = new PromotionFilter();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        if (param.containsKey("description")) {
            filter.setDescription(param.get("description"));
        }
        if (param.containsKey("promotionType")) {
            filter.setPromotionType(PromotionType.valueOf(param.get("promotionType"))); // Assuming PromotionType is an enum
        }
        if (param.containsKey("startDate")) {
            filter.setStartDate(LocalDateTime.parse(param.get("startDate"), formatter));
        }
        if (param.containsKey("endDate")) {
            filter.setEndDate(LocalDateTime.parse(param.get("endDate"), formatter));
        }

        Pageable pageable = PageUtil.getPageable(param);
        PromotionSpecification spec = new PromotionSpecification(filter);

        return promotionRepository.findAll(spec,pageable);
    }
}
