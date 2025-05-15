
package com.example.project_shopping.DTO.User;

import jakarta.validation.constraints.*;
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
    @Email(message = "Invalid email format!")
    @NotNull(message = "user must have an email")
    private String email;

    @Size(min = 8, message = "Password must be least 8 characters!")
    @NotNull(message = "password is not empty")
    private String password;

    private String photo;

    @Size(max = 45, message = "Country can't be longer than 45 characters.")
    @NotEmpty(message = "Country cannot be empty")
    private String country;

    @Size(max = 45, message = "City can't be longer than 45 characters.")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @Size(max = 100, message = "Street can't be longer than 100 characters.")
    @NotEmpty(message = "Street cannot be empty")
    private String street;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Phone number must be a valid 10-digit phone number.")
    private String phoneNumber;

}
