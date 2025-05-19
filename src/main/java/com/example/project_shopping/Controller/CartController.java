package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.Service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;

    @GetMapping("/user")
    public ResponseEntity<List<CartDTO>> getCartForUser(){
        List<CartDTO> cartDTOList = cartService.getCartForCurrentUser();
        return ResponseEntity.ok(cartDTOList);
    }
}
