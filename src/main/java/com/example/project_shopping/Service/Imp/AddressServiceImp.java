package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Address.AddressDTO;
import com.example.project_shopping.Entity.Address;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Mapper.AddressMapper;
import com.example.project_shopping.Repository.AddressRepository;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImp implements AddressService {
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private AddressMapper addressMapper;

    @Override
    public AddressDTO createAddress(Integer userId, AddressDTO addressDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);

        return addressMapper.toDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> getAddressByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return addresses.stream().map(addressMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public AddressDTO updateAddressByUserId(AddressDTO addressDTO, Integer addressID) {
        Address existingAddress = addressRepository.findById(addressID)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressID));

        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setPhoneNumber(addressDTO.getPhoneNumber());

        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.toDTO(updatedAddress);
    }


    @Override
    public void deleteAddressByUserId(Integer addressId) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressId));

        addressRepository.delete(existingAddress);
    }
}
