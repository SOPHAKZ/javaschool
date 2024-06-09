package com.javaschool.controller;

import com.javaschool.dto.course.EnrollmentDTO;
import com.javaschool.dto.course.EnrollmentRequestDTO;
import com.javaschool.entity.course.Enrollment;
import com.javaschool.mapstruct.EnrollmentMapper;
import com.javaschool.mapstruct.PageMapper;
import com.javaschool.service.course.EnrollmentService;
import com.javaschool.utils.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
@Tag(name = "Enrollment api")
public class EnrollmentController {

    private final EnrollmentMapper enrollmentMapper;
    private final EnrollmentService enrollmentService;

    @GetMapping("/{id}")
    @Operation(summary = "Get Enrollment by id")
    public ResponseEntity<EnrollmentDTO> getPromotionById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(enrollmentMapper.enrollmentDTO(enrollmentService.getEnrollmentById(id)));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Enrollment with pagination")
    public ResponseEntity<?> getPromotionList(@RequestParam Map<String,String> params){
        Page<Enrollment> enrollmentPage = enrollmentService.getAllEnrollments(params);

        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(enrollmentPage);
        pageDTO.setContent(enrollmentMapper.enrollmentDTOList(enrollmentPage.getContent()));

        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/search")
    @Operation(summary = "Search with pagination pagination key word : size, page, sortDir, sortBy")
    public ResponseEntity<?> search(@RequestParam Map<String,String> params){
        Page<Enrollment> enrollmentPage = enrollmentService.specification(params);
        PageDTO pageDTO = PageMapper.INSTANCE.pageDTO(enrollmentPage);
        pageDTO.setContent(enrollmentMapper.enrollmentDTOList(enrollmentPage.getContent()));

        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping("/ownerCourses/{userId}")
    public ResponseEntity<?> getOwnCourseByUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(
                enrollmentMapper.listCourseEnrollmentDTOs(enrollmentService.getCourseUserEnrolled(userId))
        );
    }
    @PostMapping("/add")
    @Operation(summary = "Add Promotion")
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentRequestDTO enrollmentDTO){

        return ResponseEntity.ok().body(enrollmentMapper.enrollmentDTO(
                enrollmentService.enrollUserInCourses(enrollmentDTO.getUserId(),enrollmentDTO.getCoursesId(),enrollmentDTO.getPromotionId())
        ));

    }
}
