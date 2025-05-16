package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Address.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAddressByUserId();

    AddressDTO updateAddressByUserId(AddressDTO addressDTO, Integer addressId);

    void deleteAddressByUserId( Integer addressId);
}
