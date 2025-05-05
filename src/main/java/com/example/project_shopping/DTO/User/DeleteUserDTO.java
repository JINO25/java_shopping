package com.example.project_shopping.DTO.User;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserDTO {
    @NotNull
    private Integer id;
}
