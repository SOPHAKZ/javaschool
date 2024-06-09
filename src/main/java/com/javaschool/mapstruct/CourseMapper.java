package com.javaschool.mapstruct;

import com.javaschool.dto.course.CourseDTO;

import com.javaschool.entity.course.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "categoryId",source = "category.id")
    CourseDTO courseDTO(Course course);

    @Mapping(target = "category.id",source = "categoryId")
    Course courseEntity(CourseDTO courseDTO);

    default List<CourseDTO> listCourseDTO(List<Course> courses){
        return courses.stream()
                .map(this::courseDTO)
                .toList();
    }
}
