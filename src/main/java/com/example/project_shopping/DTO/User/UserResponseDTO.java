package com.example.project_shopping.DTO.User;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String address;
    private String photo;
    private LocalDate createAt;
//    private Integer roleId;
    private String roleName;
}

