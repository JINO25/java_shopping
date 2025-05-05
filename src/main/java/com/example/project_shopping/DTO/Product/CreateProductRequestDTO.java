package com.example.project_shopping.DTO.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequestDTO {
    private String name;
    private String description;
    private Integer userId;
    private String categoryName;

    private String option;
    private String color;
    private Double price;
    private Integer stock;
}
