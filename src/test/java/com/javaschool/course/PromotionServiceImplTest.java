package com.javaschool.course;

import com.javaschool.dto.course.PromotionDTO;
import com.javaschool.entity.course.Promotion;
import com.javaschool.enums.PromotionType;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.PromotionMapper;
import com.javaschool.repository.PromotionRepository;
import com.javaschool.service.course.impl.PromotionServiceImpl;
import com.javaschool.spec.promotion.PromotionFilter;
import com.javaschool.spec.promotion.PromotionSpecification;
import com.javaschool.utils.PageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceImplTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private PromotionDTO promotionDTO;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotionDTO = new PromotionDTO();
        promotionDTO.setDescription("Test Promotion");
        promotionDTO.setPromotionType(PromotionType.BUY_X_DISCOUNT);
        promotionDTO.setStartDate(LocalDateTime.now());
        promotionDTO.setEndDate(LocalDateTime.now().plusDays(7));

        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setDescription("Test Promotion");
        promotion.setPromotionType(PromotionType.BUY_X_GET_Y_FREE);
        promotion.setStartDate(LocalDateTime.now());
        promotion.setEndDate(LocalDateTime.now().plusDays(7));
    }

    @Test
    void testCreatePromotion_Success() {
        when(promotionMapper.promotion(promotionDTO)).thenReturn(promotion);
        when(promotionRepository.save(promotion)).thenReturn(promotion);

        Promotion result = promotionService.createPromotion(promotionDTO);

        assertNotNull(result);
        assertEquals(promotion, result);
        verify(promotionRepository).save(promotion);
    }

    @Test
    void testGetPromotionById_NotFound() {
        when(promotionRepository.findById(promotion.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> promotionService.getPromotionById(promotion.getId()));
        assertEquals("Promotion", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(String.valueOf(promotion.getId()), exception.getFieldValue());
    }

    @Test
    void testGetPromotionById_Success() {
        when(promotionRepository.findById(promotion.getId())).thenReturn(Optional.of(promotion));

        Promotion result = promotionService.getPromotionById(promotion.getId());

        assertNotNull(result);
        assertEquals(promotion, result);
    }

    @Test
    void testGetAllPromotions_Success() {
        Page<Promotion> page = new PageImpl<>(Collections.singletonList(promotion));

        when(promotionRepository.findAll(any(Pageable.class))).thenReturn(page);

        Map<String, String> pageRequest = Collections.emptyMap();
        Page<Promotion> result = promotionService.getAllPromotions(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(promotion, result.getContent().get(0));
    }

    @Test
    void testSpecificationSearch_Success() {
        Map<String, String> param = Collections.singletonMap("description", "Test");
        PromotionFilter filter = new PromotionFilter();
        filter.setDescription("Test");
        Page<Promotion> page = new PageImpl<>(Collections.singletonList(promotion));
        PromotionSpecification spec = new PromotionSpecification(filter);

        when(promotionRepository.findAll(any(PromotionSpecification.class), any(Pageable.class))).thenReturn(page);

        Page<Promotion> result = promotionService.specificationSearch(param);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(promotion, result.getContent().get(0));
    }
}
