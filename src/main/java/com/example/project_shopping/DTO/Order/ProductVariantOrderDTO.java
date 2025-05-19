package com.example.project_shopping.DTO.Order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantOrderDTO {
    private Integer id;
    private String option;
    private String color;
    private Double price;
    private Integer quantity;
    private String productName;
}
