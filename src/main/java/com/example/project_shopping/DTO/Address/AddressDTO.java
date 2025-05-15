package com.example.project_shopping.DTO.Address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    Long id;
    String country;
    String city;
    String street;
    String phoneNumber;
}
