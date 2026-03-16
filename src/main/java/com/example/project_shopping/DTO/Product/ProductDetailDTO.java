package com.example.project_shopping.DTO.Product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private String sellerName;
    private String categoryName;
    private List<ProductVariantDTO> variants;
    private List<String> images;
}

