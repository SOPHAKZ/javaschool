package com.javaschool.mapstruct;

import com.javaschool.dto.course.PaymentDTO;
import com.javaschool.entity.course.Payment;
import com.javaschool.service.course.CourseService;
import com.javaschool.service.course.EnrollmentService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = {EnrollmentService.class})
public interface PaymentMapper {

    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "enrollment.id",target = "enrollmentId")
    PaymentDTO paymentDTO(Payment payment);

//    @Mapping(source = "userId",target = "user.id")
////    @Mapping(source = "enrollmentId",target = "enrollment.id")
//    Payment payment(PaymentDTO paymentDTO);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "enrollmentId", target = "enrollment.id")
    Payment payment(PaymentDTO paymentDto);

    List<PaymentDTO> paymentDTOList(List<PaymentDTO> paymentDTOList);
}
