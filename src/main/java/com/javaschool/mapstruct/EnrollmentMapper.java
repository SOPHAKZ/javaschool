package com.javaschool.mapstruct;

import com.javaschool.dto.course.CourseEnrollmentDTO;
import com.javaschool.dto.course.EnrollmentDTO;

import com.javaschool.entity.course.Course;
import com.javaschool.entity.course.Enrollment;

import com.javaschool.service.course.CourseService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {CourseService.class})
public interface EnrollmentMapper {

    @Mapping(source = "courses",target ="courseIds")
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "promotion.id",target = "promotionId")
    EnrollmentDTO enrollmentDTO(Enrollment enrollment);

    @Mapping(source = "id",target = "enrollmentId")
    @Mapping(source = "isCompleted",target = "isCompleted")
    @Mapping(source = "courses",target = "courseIds")
    CourseEnrollmentDTO courseEnrollmentDTO(Enrollment enrollment);

    default List<Long> mapCourseToIds(List<Course> courses){
        return courses.stream()
                .map(Course::getId)
                .toList();
    }
    List<EnrollmentDTO> enrollmentDTOList(List<Enrollment> enrollments);

    default List<CourseEnrollmentDTO> enrollmentsToCourseEnrollmentDTOs(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(this::courseEnrollmentDTO)
                .collect(Collectors.toList());
    }

    List<CourseEnrollmentDTO> listCourseEnrollmentDTOs(List<Enrollment> enrollments);

}
