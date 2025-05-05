package com.example.project_shopping.DTO.Product;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {
    private Integer id;
    @NotEmpty(message = "Product name cannot be empty")
    private String name;
    private String description;
    private String userName;
    private String categoryName;
    private List<ProductVariantDTO> productVariants;
}
