package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Address.AddressDTO;
import com.example.project_shopping.Service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/{userId}")
    public ResponseEntity<AddressDTO> createAddress(
            @PathVariable Integer userId,
            @RequestBody AddressDTO addressDTO) {
        AddressDTO createdAddress = addressService.createAddress(userId, addressDTO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressByUserId(@PathVariable Long userId) {
        List<AddressDTO> addressDTO = addressService.getAddressByUserId(userId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AddressDTO> updateAddress(
            @RequestBody AddressDTO addressDTO,
            @RequestParam(name = "addressId") Integer address) {
        AddressDTO updated = addressService.updateAddressByUserId(addressDTO, address);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAddress(@RequestParam(name = "addressId") Integer addressId) {
        addressService.deleteAddressByUserId(addressId);
        return ResponseEntity.noContent().build();
    }
}
