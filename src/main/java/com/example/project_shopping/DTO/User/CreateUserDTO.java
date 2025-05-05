
package com.example.project_shopping.DTO.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @Size(max = 45)
    @NotNull(message = "user must have a name")
    private String name;

    @Size(max = 45)
    @NotNull(message = "user must have an email")
    private String email;

    @Size(max = 45)
    @NotNull(message = "password is not empty")
    private String password;

    @Size(max = 45)
    @NotNull(message = "user must have an address")
    private String address;

    @Size(max = 45)
    private String photo;

}
