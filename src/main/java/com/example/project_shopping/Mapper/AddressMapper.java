package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Address.AddressDTO;
import com.example.project_shopping.Entity.Address;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AddressMapper {
    private ModelMapper modelMapper;

    public AddressDTO toDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }

    public Address toEntity(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, Address.class);
    }

    public List<AddressDTO> addressDTOList(Optional<Address> address){
        return address.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
