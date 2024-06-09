package com.javaschool.controller;

import com.javaschool.dto.course.PaymentDTO;
import com.javaschool.mapstruct.PaymentMapper;
import com.javaschool.service.course.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Tag(name = "Payment api")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;


    @PostMapping("/")
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO){

        return ResponseEntity.ok().body(paymentMapper.paymentDTO(paymentService.createPayment(paymentDTO)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a payment by id")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id){
        return ResponseEntity.ok().body(paymentMapper.paymentDTO(paymentService.getPaymentById(id)));
    }


}
