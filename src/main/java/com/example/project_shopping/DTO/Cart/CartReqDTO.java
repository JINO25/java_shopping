package com.example.project_shopping.DTO.Cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartReqDTO {
    private Integer productVariantId;
    @NotNull(message = "Order must have quantity")
    @Min(value = 1, message = "quantity must be least 1")
    private Integer quantity;
}
