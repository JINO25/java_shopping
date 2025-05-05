package com.example.project_shopping.DTO.User;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateUserDTO {
    @Size(max = 45)
    private String name;

    @Size(max = 45)
    private String email;

    @Size(max = 45)
    private String address;

    @Size(max = 45)
    private String photo;

}
