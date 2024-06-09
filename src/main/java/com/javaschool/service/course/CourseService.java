package com.javaschool.service.course;

import com.javaschool.dto.course.CourseDTO;

import com.javaschool.entity.course.Course;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CourseService {

    Course createCourse(CourseDTO courseDTO);
    Course getCourseById(Long id);
    Page<Course> getAllCourse(Map<String,String> pageRequest);
    Page<Course> specificationSearch(Map<String,String> param);

}
