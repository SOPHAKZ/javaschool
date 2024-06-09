package com.javaschool.spec.Course;

import com.javaschool.entity.course.Category;
import com.javaschool.entity.course.Course;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CourseSpecification implements Specification<Course> {

    private final CourseFilter courseFilter;

    @Override
    public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        Join<Course, Category> categoryJoin = root.join("category");

        if(courseFilter.getId() != null){
            predicates.add(cb.equal(root.get("id"),courseFilter.getId()));
        }
        if(courseFilter.getName() != null){
            predicates.add(cb.like(root.get("name"), "%"+courseFilter.getName()));
        }
        if(courseFilter.getIsOnline() != null){
            predicates.add(cb.equal(root.get("isOnline"),courseFilter.getIsOnline()));
        }
        if(courseFilter.getCategoryId() != null){
            predicates.add(cb.equal(categoryJoin.get("id"),courseFilter.getCategoryId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
