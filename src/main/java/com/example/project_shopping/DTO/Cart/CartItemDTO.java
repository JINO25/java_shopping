package com.example.project_shopping.DTO.Cart;

import com.example.project_shopping.Enums.CartStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Integer id;
    private Integer quantity;
    private CartStatus cartStatus;
    private ProductVariantCartDTO productVariantCartDTO;

}
