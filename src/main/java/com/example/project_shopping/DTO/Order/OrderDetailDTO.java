package com.example.project_shopping.DTO.Order;

import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailDTO {
    private Integer id;
    private Integer quantity;
    private Double price;
    private ProductVariantOrderDTO productVariantOrderDTO;
}
