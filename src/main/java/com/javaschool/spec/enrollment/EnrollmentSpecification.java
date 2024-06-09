package com.javaschool.spec.enrollment;

import com.javaschool.entity.course.Enrollment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class EnrollmentSpecification implements Specification<Enrollment> {

    private final EnrollmentFilter filter;

    public EnrollmentSpecification(EnrollmentFilter filter) {
        this.filter = filter;
    }


    @Override
    public Predicate toPredicate(Root<Enrollment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), filter.getId()));
        }
        if (filter.getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.join("user").get("id"), filter.getUserId()));
        }
        if (filter.getPromotionId() != null) {
            predicates.add(criteriaBuilder.equal(root.join("promotion").get("id"), filter.getPromotionId()));
        }
        if (filter.getIsCompleted() != null) {
            predicates.add(criteriaBuilder.equal(root.get("isCompleted"), filter.getIsCompleted()));
        }
        if (filter.getFromDate() != null && filter.getToDate() != null) {
            predicates.add(criteriaBuilder.between(root.get("enrollmentDate"), filter.getFromDate(), filter.getToDate()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
