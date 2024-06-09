package com.javaschool.service.course.impl;

import com.javaschool.dto.course.CourseEnrollmentDTO;
import com.javaschool.entity.course.Course;
import com.javaschool.entity.course.Enrollment;
import com.javaschool.entity.course.Promotion;
import com.javaschool.entity.user.User;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.repository.course.CourseRepository;
import com.javaschool.repository.course.EnrollmentRepository;

import com.javaschool.service.course.EnrollmentService;
import com.javaschool.service.course.PromotionService;
import com.javaschool.service.user.UserService;
import com.javaschool.spec.enrollment.EnrollmentFilter;
import com.javaschool.spec.enrollment.EnrollmentSpecification;
import com.javaschool.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final PromotionService promotionService;
    private final UserService userService;


    @Override
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrolment","id",id.toString()));
    }

    @Override
    public Enrollment enrollUserInCourses(Long userId, List<Long> coursesIds, Long promotionId) {
        List<Course> courses = courseRepository.findAllById(coursesIds);
        User user = userService.findById(userId);
        Promotion promotion = null;
        if (promotionId != null) {
            promotion = promotionService.getPromotionById(promotionId);
        }

        double totalPrice = calculateTotalPrice(courses, promotion);

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setPaymentPrice(totalPrice);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        if (promotion != null) {
            enrollment.setPromotion(promotion);
            List<Course> freeCourses = courseRepository.findAllById(promotion.getReward().getFreeCourseIds());
            courses.addAll(freeCourses);
        }

        enrollment.setCourses(courses);

        return enrollmentRepository.save(enrollment);
    }



    @Override
    public Enrollment updateEnrollmentStatus(Long id,boolean isCompleted) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setIsCompleted(isCompleted);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getCourseUserEnrolled(Long userId) {
        List<Enrollment> byUserIdAndIsCompletedTrue = enrollmentRepository.findByUser_IdAndIsCompletedTrue(userId);

        log.info("COURSE ID :" +byUserIdAndIsCompletedTrue.stream()
                .map(Enrollment::getCourses)
                .toList()
        );
        return byUserIdAndIsCompletedTrue;
    }

    private double calculateTotalPrice(List<Course> courses, Promotion promotion) {
        if(promotion == null){
            return courses.stream().mapToDouble(Course::getPrice).sum();
        }

        switch (promotion.getPromotionType()){
            case BUY_X_GET_Y_FREE:
                return applyBuyXGetYFree(courses,promotion);
            case BUY_X_DISCOUNT:
                return appyBuyXDicount(courses,promotion);
//            case BUY_X_PAY_FOR_Y:
//                return applyBuyXPayForY(courses,promotion);
            default:
                return 0;
        }


    }

    private double applyBuyXGetYFree(List<Course> courses, Promotion promotion) {
        Set<Long> freeCourseIds = promotion.getReward().getFreeCourseIds();

        int requiredCourse = promotion.getRequiredCourses();
        int freeCourse = freeCourseIds.size();

        courses.sort(Comparator.comparingDouble(Course::getPrice).reversed());
        double totalPrice = 0;
        for(int i = 0 ; i <courses.size(); i++){
            if(i < requiredCourse){
                totalPrice += courses.get(i).getPrice();
            }else if(i >= requiredCourse && i < requiredCourse + freeCourse){

                // check course is free not + price to total price
                if(!freeCourseIds.contains(courses.get(i).getId())){
                    totalPrice += courses.get(i).getPrice();
                }
            }else{
                totalPrice += courses.get(i).getPrice();
            }
        }
        return totalPrice;
    }

    private double appyBuyXDicount(List<Course> courses, Promotion promotion) {
        int requiredCourses = promotion.getRequiredCourses();
        double discountPercentage = promotion.getReward().getDiscountPercentage();

        double totalPrice = courses.stream().mapToDouble(Course::getPrice).sum();

        if(courses.size() >= requiredCourses){
            totalPrice -= totalPrice * discountPercentage/100;
        }

        return totalPrice;
    }

    @Override
    public Page<Enrollment> getAllEnrollments(Map<String, String> params) {
        Pageable pageable = PageUtil.getPageable(params);

        return enrollmentRepository.findAll(pageable);
    }

    @Override
    public Page<Enrollment> specification(Map<String, String> param) {
        EnrollmentFilter filter = new EnrollmentFilter();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        if (param.containsKey("id")) {
            filter.setId(Long.valueOf(param.get("id")));
        }
        if (param.containsKey("userId")) {
            filter.setUserId(Long.valueOf(param.get("userId"))); // Assuming PromotionType is an enum
        }
        if (param.containsKey("startDate")) {
            filter.setIsCompleted(Boolean.valueOf(param.get("isCompleted")));
        }
        if (param.containsKey("fromDate")) {
            filter.setFromDate(LocalDateTime.parse(param.get("fromDate"),formatter));
        }
        if (param.containsKey("toDate")) {
            filter.setFromDate(LocalDateTime.parse(param.get("toDate"),formatter));
        }
        Pageable pageable = PageUtil.getPageable(param);
        EnrollmentSpecification spec = new EnrollmentSpecification(filter);

        return enrollmentRepository.findAll(spec,pageable);
    }
}
