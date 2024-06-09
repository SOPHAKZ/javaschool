package com.javaschool.course;

import com.javaschool.entity.course.Course;
import com.javaschool.entity.course.Enrollment;
import com.javaschool.entity.course.Promotion;
import com.javaschool.entity.user.User;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.repository.course.CourseRepository;
import com.javaschool.repository.course.EnrollmentRepository;
import com.javaschool.service.course.impl.EnrollmentServiceImpl;
import com.javaschool.service.course.PromotionService;
import com.javaschool.service.user.UserService;
import com.javaschool.spec.enrollment.EnrollmentFilter;
import com.javaschool.spec.enrollment.EnrollmentSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PromotionService promotionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Enrollment enrollment;
    private Course course;
    private User user;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setPrice(100.0);

        user = new User();
        user.setId(1L);

        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setRequiredCourses(2);

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setUser(user);
        enrollment.setCourses(Collections.singletonList(course));
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> enrollmentService.getEnrollmentById(enrollment.getId()));
        assertEquals("Enrolment", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(String.valueOf(enrollment.getId()), exception.getFieldValue());
    }

    @Test
    void testGetEnrollmentById_Success() {
        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(java.util.Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentById(enrollment.getId());

        assertNotNull(result);
        assertEquals(enrollment, result);
    }

    // Test enrollUserInCourses, updateEnrollmentStatus, getCourseUserEnrolled, calculateTotalPrice, applyBuyXGetYFree, applyBuyXDicount

    @Test
    void testGetAllEnrollments_Success() {
        Page<Enrollment> page = new PageImpl<>(Collections.singletonList(enrollment));

        when(enrollmentRepository.findAll(any(Pageable.class))).thenReturn(page);

        Map<String, String> params = Collections.emptyMap();
        Page<Enrollment> result = enrollmentService.getAllEnrollments(params);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(enrollment, result.getContent().get(0));
    }

    @Test
    void testSpecification_Success() {
        Map<String, String> params = Collections.singletonMap("userId", "1");
        EnrollmentFilter filter = new EnrollmentFilter();
        filter.setUserId(1L);
        Page<Enrollment> page = new PageImpl<>(Collections.singletonList(enrollment));
        EnrollmentSpecification spec = new EnrollmentSpecification(filter);

        when(enrollmentRepository.findAll(any(EnrollmentSpecification.class), any(Pageable.class))).thenReturn(page);

        Page<Enrollment> result = enrollmentService.specification(params);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(enrollment, result.getContent().get(0));
    }
}
