package com.javaschool.course;

import com.javaschool.dto.course.PaymentDTO;
import com.javaschool.entity.course.Enrollment;
import com.javaschool.entity.course.Payment;
import com.javaschool.enums.PaymentStatus;
import com.javaschool.exception.ResourceNotFoundException;
import com.javaschool.mapstruct.PaymentMapper;
import com.javaschool.repository.course.PaymentRepository;
import com.javaschool.service.course.EnrollmentService;
import com.javaschool.service.course.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentDTO paymentDTO;
    private Payment payment;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        paymentDTO = new PaymentDTO();
        paymentDTO.setStatus(PaymentStatus.PAID);
        paymentDTO.setEnrollmentId(1L);

        payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.PAID);

        enrollment = new Enrollment();
        enrollment.setId(1L);
    }

//    @Test
//    void testCreatePayment_Success() {
//        // Mock the enrollment object
//        Enrollment enrollment = new Enrollment();
//        enrollment.setId(1L); // Set a valid ID for the enrollment
//
//        when(enrollmentService.getEnrollmentById(anyLong())).thenReturn(enrollment);
//
//        // Mock the payment mapper and repository
//        when(paymentMapper.payment(paymentDTO)).thenReturn(payment);
//        when(paymentRepository.save(payment)).thenReturn(payment);
//
//        // Call the method under test
//        Payment result = paymentService.createPayment(paymentDTO);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(payment, result);
//        verify(enrollmentService, times(1)).updateEnrollmentStatus(enrollment.getId(), true);
//    }
//

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(payment.getId())).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentById(payment.getId()));
        assertEquals("Payment", exception.getResourceName());
        assertEquals("id", exception.getResourceFiled());
        assertEquals(String.valueOf(payment.getId()), exception.getFieldValue());
    }

    @Test
    void testGetPaymentById_Success() {
        when(paymentRepository.findById(payment.getId())).thenReturn(java.util.Optional.of(payment));

        Payment result = paymentService.getPaymentById(payment.getId());

        assertNotNull(result);
        assertEquals(payment, result);
    }
}
