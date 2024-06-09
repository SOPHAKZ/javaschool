package com.javaschool.entity.course;

import com.javaschool.config.audit.UserDateAudit;
import com.javaschool.entity.user.User;
import com.javaschool.enums.PaymentOption;
import com.javaschool.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Assume Student entity exists

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentOption paymentOption;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paidDate;

}
