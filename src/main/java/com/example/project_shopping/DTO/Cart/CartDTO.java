package com.example.project_shopping.DTO.Cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CartDTO {
    private Integer id;
    private String userName;
    private List<CartItemDTO> cartItems;
}
