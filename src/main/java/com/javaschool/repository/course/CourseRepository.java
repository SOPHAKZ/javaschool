package com.javaschool.repository.course;


import com.javaschool.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CourseRepository extends JpaRepository<Course,Long> , JpaSpecificationExecutor {
}
