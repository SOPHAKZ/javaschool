package com.javaschool.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Enrollment enrollment;


    private Double amount;
    
    private Date paymentDate;

    private String paymentMethod;  // e.g., "Credit Card", "Cash"

    // Getters and setters omitted for brevity
}