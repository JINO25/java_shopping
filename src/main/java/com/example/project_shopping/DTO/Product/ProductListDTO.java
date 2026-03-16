package com.example.project_shopping.DTO.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListDTO {
    private Integer id;
    private String name;
    private String thumbnail;
    private Double minPrice;
    private Double maxPrice;
    private String categoryName;
}

