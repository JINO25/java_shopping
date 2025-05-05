package com.example.project_shopping.DTO.Category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private Integer id;
    @NotNull(message = "Category must have name")
    private String name;
}
