package com.example.project_shopping.DTO.Order;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartItemeqDTO {
    @NotEmpty(message = "Cart item IDs cannot be empty")
    private List<Integer> cartItemIds;
}
