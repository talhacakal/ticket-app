package com.payment.Entity.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Integer id;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private String paymentType;

}