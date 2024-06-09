package com.javaschool.controller;

import com.javaschool.dto.course.CourseDTO;

import com.javaschool.entity.course.Course;
import com.javaschool.mapstruct.CourseMapper;
import com.javaschool.mapstruct.PageMapper;
import com.javaschool.service.course.CourseService;
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
@RequestMapping("/api/course")
@Tag(name = "Course api")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping("/{id}")
    @Operation(summary = "get course by id")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(courseMapper.courseDTO(courseService.getCourseById(id)));
    }

    @PostMapping("/")
    @Operation(summary = "create course")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO){
        return ResponseEntity.ok().body(courseMapper.courseDTO(courseService.createCourse(courseDTO)));
    }


    @GetMapping("/all")
    @Operation(summary = "Get all courses with pagination")
    public ResponseEntity<?> getCourseList(@RequestParam Map<String,String> param){

        Page<Course> allCourse = courseService.getAllCourse(param);

        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(allCourse);
        pageDTO.setContent(courseMapper.listCourseDTO(allCourse.getContent()));
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search Course with pagination pagination key word : size, page, sortDir, sortBy")
    public ResponseEntity<?> specificationSearch(@RequestParam Map<String,String> param){
        Page<Course> courses = courseService.specificationSearch(param);

        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(courses);
        pageDTO.setContent(courseMapper.listCourseDTO(courses.getContent()));

        return ResponseEntity.ok().body(pageDTO);
    }
}
