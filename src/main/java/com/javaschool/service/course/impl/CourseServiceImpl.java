package com.javaschool.service.course.impl;

import com.javaschool.dto.course.CourseDTO;
import com.javaschool.entity.course.Course;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.CourseMapper;
import com.javaschool.repository.course.CategoryRepository;
import com.javaschool.repository.course.CourseRepository;
import com.javaschool.service.course.CourseService;
import com.javaschool.spec.Course.CourseFilter;
import com.javaschool.spec.Course.CourseSpecification;
import com.javaschool.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    private final CourseRepository courseRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Course createCourse(CourseDTO courseDTO) {
        if(!categoryRepository.existsById(courseDTO.getCategoryId())){
            throw new ResourceNotFoundException("Category","id",String.valueOf(courseDTO.getCategoryId()));
        }
        return courseRepository.save(courseMapper.courseEntity(courseDTO));

    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Course","id",id.toString()));
    }

    @Override
    public Page<Course> getAllCourse(Map<String,String> pageRequest) {
        Pageable pageable = PageUtil.getPageable(pageRequest);

        Page<Course> dataList = courseRepository.findAll(pageable);

        return dataList;

    }

    @Override
    public Page<Course> specificationSearch(Map<String, String> param) {
        CourseFilter filter = new CourseFilter();
        if(param.containsKey("id")){
            filter.setId(Long.valueOf(param.get("id")));
        }
        if(param.containsKey("name")){
            filter.setName(param.get("name"));
        }
        if(param.containsKey("isOnline")){
            filter.setIsOnline(Boolean.valueOf(param.get("isOnline")));
        }
        if(param.containsKey("categoryId")){
            filter.setCategoryId(Long.valueOf(param.get("categoryId")));
        }


        Pageable pageable = PageUtil.getPageable(param);
        CourseSpecification spec = new CourseSpecification(filter);

        return courseRepository.findAll(spec,pageable);

    }


}
