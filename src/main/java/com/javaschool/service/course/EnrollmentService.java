package com.javaschool.service.course;

import com.javaschool.dto.course.CourseEnrollmentDTO;
import com.javaschool.entity.course.Enrollment;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface EnrollmentService {
    Enrollment getEnrollmentById(Long id);
    Enrollment enrollUserInCourses(Long userId, List<Long> coursesIds, Long promotionId);
    Enrollment updateEnrollmentStatus(Long id,boolean isCompleted);
    List<Enrollment> getCourseUserEnrolled(Long userId);
    Page<Enrollment> getAllEnrollments(Map<String,String> params);
    Page<Enrollment> specification(Map<String,String> params);
}
