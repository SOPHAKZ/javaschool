package com.javaschool.course;

import com.javaschool.dto.course.CourseDTO;
import com.javaschool.entity.course.Course;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.CourseMapper;
import com.javaschool.repository.course.CategoryRepository;
import com.javaschool.repository.course.CourseRepository;
import com.javaschool.service.course.impl.CourseServiceImpl;
import com.javaschool.spec.Course.CourseFilter;
import com.javaschool.spec.Course.CourseSpecification;
import com.javaschool.utils.PageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseDTO courseDTO;
    private Course course;

    @BeforeEach
    void setUp() {
        courseDTO = new CourseDTO();
        courseDTO.setCategoryId(1L);
        courseDTO.setName("Test Course");

        course = new Course();
        course.setId(1L);
        course.setName("Test Course");
    }

    @Test
    void testCreateCourse_CategoryNotFound() {
        when(categoryRepository.existsById(courseDTO.getCategoryId())).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> courseService.createCourse(courseDTO));
        assertEquals("Category", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(String.valueOf(courseDTO.getCategoryId()), exception.getFieldValue());
    }

    @Test
    void testCreateCourse_Success() {
        when(categoryRepository.existsById(courseDTO.getCategoryId())).thenReturn(true);
        when(courseMapper.courseEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.createCourse(courseDTO);

        assertNotNull(result);
        assertEquals(course, result);
        verify(courseRepository).save(course);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(course.getId()));
        assertEquals("Course", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(String.valueOf(course.getId()), exception.getFieldValue());
    }

    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(course.getId());

        assertNotNull(result);
        assertEquals(course, result);
    }

    @Test
    void testGetAllCourse_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(Collections.singletonList(course), pageable, 1);

        when(courseRepository.findAll(any(Pageable.class))).thenReturn(page); // Adjusted stubbing

        Map<String, String> pageRequest = Collections.emptyMap();
        Page<Course> result = courseService.getAllCourse(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(course, result.getContent().get(0));
    }

    @Test
    void testSpecificationSearch_Success() {
        Map<String, String> param = Collections.singletonMap("id", "1");
        CourseFilter filter = new CourseFilter();
        filter.setId(1L);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<Course> page = new PageImpl<>(Collections.singletonList(course), pageable, 1);
        CourseSpecification spec = new CourseSpecification(filter);

        when(courseRepository.findAll(any(CourseSpecification.class), any(Pageable.class))).thenReturn(page); // Adjusted stubbing

        Page<Course> result = courseService.specificationSearch(param);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(course, result.getContent().get(0));
    }


//    @Test
//    void testSpecificationSearch_Success() {
//        Map<String, String> param = Collections.singletonMap("id", "1");
//        CourseFilter filter = new CourseFilter();
//        filter.setId(1L);
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Course> page = new PageImpl<>(Collections.singletonList(course), pageable, 1);
//        CourseSpecification spec = new CourseSpecification(filter);
//
//        when(courseRepository.findAll(any(CourseSpecification.class), eq(pageable))).thenReturn(page);
//
//        Page<Course> result = courseService.specificationSearch(param);
//
//        assertNotNull(result);
//        assertEquals(1, result.getTotalElements());
//        assertEquals(course, result.getContent().get(0));
//    }
}
