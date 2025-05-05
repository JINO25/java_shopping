package com.example.project_shopping.DTO.Product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantDTO {
    private Integer id;
    private String option;
    private String color;
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be a positive value")
    private Double price;
    @NotNull(message = "Stock cannot be null")
    @Positive(message = "Stock must be a positive value")
    private Integer stock;
}
