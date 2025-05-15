package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Address.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(Integer userId, AddressDTO addressDTO);

    List<AddressDTO> getAddressByUserId(Long userId);

    AddressDTO updateAddressByUserId(AddressDTO addressDTO, Integer addressId);

    void deleteAddressByUserId( Integer addressId);
}
