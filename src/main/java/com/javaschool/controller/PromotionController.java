package com.javaschool.controller;

import com.javaschool.dto.course.PromotionDTO;
import com.javaschool.entity.course.Promotion;
import com.javaschool.mapstruct.PageMapper;
import com.javaschool.mapstruct.PromotionMapper;
import com.javaschool.service.course.PromotionService;
import com.javaschool.utils.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/promotion")
@Tag(name = "Promotion api")
public class PromotionController {

    private final PromotionMapper promotionMapper;
    private final PromotionService promotionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by id")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(promotionMapper.promotionDTO(promotionService.getPromotionById(id)));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all with pagination pagination key word : size, page, sortDir, sortBy")
    public ResponseEntity<?> getPromotionList(@RequestParam Map<String,String> params){
        Page<Promotion> promotionPage = promotionService.getAllPromotions(params);

        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(promotionPage);
        pageDTO.setContent(promotionMapper.promotionDTOList(promotionPage.getContent()));

        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Course with pagination pagination key word : size, page, sortDir, sortBy")
    public ResponseEntity<?> search(@RequestParam Map<String,String> params){
        Page<Promotion> promotionPage = promotionService.specificationSearch(params);
        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(promotionPage);
        pageDTO.setContent(promotionMapper.promotionDTOList(promotionPage.getContent()));

        return ResponseEntity.ok().body(pageDTO);
    }

    @PostMapping("/add")
    @Operation(summary = "Add Promotion")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO){
        return ResponseEntity.ok().body(promotionMapper.promotionDTO(promotionService.createPromotion(promotionDTO)));
    }
}
