package com.example.project_shopping.DTO.Category;

import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.Entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryWithProductListDTO {
    private Integer id;
    private String name;
    private List<Product> productList;
}
