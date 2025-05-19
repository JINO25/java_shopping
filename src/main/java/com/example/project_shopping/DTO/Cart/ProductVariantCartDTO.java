package com.example.project_shopping.DTO.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantCartDTO {
    private Integer id;
    private String productName;
    private String option;
    private String color;
    private Double price;
}
