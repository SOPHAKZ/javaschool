package com.javaschool.dto.course;

import com.javaschool.enums.PaymentOption;
import com.javaschool.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long userId;
    private Long enrollmentId;
    private Double amount;
    private PaymentOption paymentOption;
    private PaymentStatus status;
    private LocalDateTime paidDate;

}
