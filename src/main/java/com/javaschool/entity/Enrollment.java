package com.javaschool.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private Student student;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Promotion promotion;  // Optional, for promotion-based enrollment

    private Date enrollmentDate;

    private String paymentStatus;

    private String paymentOption;

    private Double totalAmount;

    private Double paidAmount;

}