package com.javaschool.mapstruct;

import com.javaschool.dto.course.PromotionDTO;
import com.javaschool.dto.course.RewardDTO;
import com.javaschool.entity.course.Course;
import com.javaschool.entity.course.Promotion;
import com.javaschool.entity.course.Reward;
import com.javaschool.service.course.CourseService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring",uses = {CourseService.class})
public interface PromotionMapper {


    @Mapping(source = "courses",target ="courseIds")
    @Mapping(source = "reward",target = "rewardId")
    PromotionDTO promotionDTO(Promotion promotion);

    @Mapping(source = "courseIds",target = "courses")
    @Mapping(source = "rewardId",target = "reward")
    Promotion promotion(PromotionDTO promotionDTO);


    List<PromotionDTO> promotionDTOList(List<Promotion> promotion);

    default List<Long> mapCourseToIds(List<Course> courses){
        return courses.stream()
                .map(Course::getId)
                .toList();
    }


}
