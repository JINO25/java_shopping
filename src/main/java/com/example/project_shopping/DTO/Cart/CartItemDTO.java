package com.example.project_shopping.DTO.Cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Integer id;
    private Integer quantity;
    private ProductVariantCartDTO productVariantCartDTO;

}
