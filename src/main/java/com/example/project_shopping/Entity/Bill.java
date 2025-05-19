package com.example.project_shopping.Entity;

import com.example.project_shopping.Enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "total")
    private Double total;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Size(max = 45)
    @Column(name = "method", length = 45)
    private String method;


    @Column(name = "payment_status", length = 45)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_time")
    private LocalDate paymentTime;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Order_id", nullable = false)
    private Order order;

}