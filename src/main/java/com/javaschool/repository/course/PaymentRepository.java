package com.javaschool.repository.course;

import com.javaschool.entity.course.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

}
