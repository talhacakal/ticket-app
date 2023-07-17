package com.payment.Controller;

import com.payment.Entity.DTO.PaymentDTO;
import com.payment.Entity.Payment;
import com.payment.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @PostMapping("")
    public ResponseEntity createPayment(@RequestParam BigDecimal amount, @RequestParam String paymentType){
        return ResponseEntity.ok(
                this.paymentRepository.save(new Payment(amount,paymentType))
        );
    }

}
