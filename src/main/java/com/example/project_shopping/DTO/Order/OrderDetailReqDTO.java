package com.example.project_shopping.DTO.Order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailReqDTO {
    private Integer productVariantId;
    @NotNull(message = "Order must have quantity")
    @Min(value = 1, message = "quantity must be least 1")
    private Integer quantity;
}
