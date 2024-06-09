package com.javaschool.service.course;

import com.javaschool.dto.course.PaymentDTO;
import com.javaschool.entity.course.Payment;

public interface PaymentService {
    Payment createPayment(PaymentDTO paymentDTO);
    Payment getPaymentById(Long id);
}
