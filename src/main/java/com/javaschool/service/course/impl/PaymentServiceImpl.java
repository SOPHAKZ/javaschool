package com.javaschool.service.course.impl;

import com.javaschool.dto.course.PaymentDTO;
import com.javaschool.entity.course.Payment;

import com.javaschool.enums.PaymentStatus;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.PaymentMapper;
import com.javaschool.repository.course.PaymentRepository;
import com.javaschool.service.course.EnrollmentService;
import com.javaschool.service.course.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final EnrollmentService enrollmentService;

    @Transactional
    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {

        Payment payment = paymentMapper.payment(paymentDTO);

        payment = paymentRepository.save(payment);

        // update enrollment to completed when do payment
        enrollmentService.updateEnrollmentStatus(payment.getEnrollment().getId(), true);


        return payment;
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment","id",id.toString()));
    }

}
