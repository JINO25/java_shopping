package com.example.project_shopping.DTO.Product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Getter
//@Setter
//public class CreateProductRequestDTO {
//    private String name;
//    private String description;
////    private Integer userId;
//    private String categoryName;
//
//    private String option;
//    private String color;
//    private Double price;
//    private Integer stock;
//}
    @Getter
    @Setter
public class CreateProductRequestDTO {
    private String name;
    private String description;
    private String categoryName;
    private List<ProductVariantDTO> variants;
}
