package com.payment.Entity;

import com.payment.Entity.DTO.PaymentDTO;
import com.payment.Entity.Enum.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    public Payment(BigDecimal amount, String paymentType) {
        this.amount = amount;
        this.paymentType = PaymentType.fromString(paymentType);
    }
}
