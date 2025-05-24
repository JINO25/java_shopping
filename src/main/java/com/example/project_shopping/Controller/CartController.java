package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.DTO.Cart.CartReqDTO;
import com.example.project_shopping.Service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(@Valid @RequestBody CartReqDTO cartReqDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->{
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        CartDTO updatedCart = cartService.addItem(cartReqDTO);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartDTO> updateItemQuantity(@PathVariable Integer cartItemId,
                                                      @RequestParam Integer quantity) {
        CartDTO updatedCart = cartService.updateItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clean")
    public ResponseEntity<Void> cleanCart() {
        cartService.clearCartItem();
        return ResponseEntity.noContent().build();
    }

}
