package com.example.project_shopping.DTO.User;

import com.example.project_shopping.DTO.Address.AddressDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private String photo;
    private LocalDate createAt;
//    private Integer roleId;
    private String roleName;
    private List<AddressDTO> addresses;
}

