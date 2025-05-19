package com.example.project_shopping.DTO.Bill;

import com.example.project_shopping.Enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBillDTO {
    private Integer billID;
    private PaymentStatus paymentStatus;
}
