package com.javaschool.spec.promotion;

import com.javaschool.entity.course.Course;
import com.javaschool.entity.course.Promotion;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PromotionSpecification implements Specification<Promotion> {

    private final PromotionFilter filter;

    @Override
    public Predicate toPredicate(Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getDescription() != null && !filter.getDescription().isEmpty()) {
            predicates.add(cb.like(root.get("description"), "%" + filter.getDescription() + "%"));
        }

        if (filter.getPromotionType() != null) {
            predicates.add(cb.equal(root.get("promotionType"), filter.getPromotionType()));
        }

        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), filter.getEndDate()));
        }

        if (filter.getCourseIds() != null && !filter.getCourseIds().isEmpty()) {
            Join<Promotion, Course> courseJoin = root.join("courses");
            predicates.add(courseJoin.get("id").in(filter.getCourseIds()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
