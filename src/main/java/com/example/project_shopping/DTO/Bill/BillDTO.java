package com.example.project_shopping.DTO.Bill;

import com.example.project_shopping.DTO.Order.OrderDTO;
import com.example.project_shopping.Enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BillDTO {
    private Integer id;
    private Double total;
    private LocalDate billDate;
    private String method;
    private PaymentStatus paymentStatus;
    private LocalDate paymentTime;
    private OrderDTO orderDTO;
}
