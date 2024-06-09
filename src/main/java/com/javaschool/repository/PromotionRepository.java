package com.javaschool.repository;

import com.javaschool.entity.course.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromotionRepository extends JpaRepository<Promotion, Long> , JpaSpecificationExecutor {
}
