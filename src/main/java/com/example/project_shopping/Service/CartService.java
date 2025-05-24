package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.DTO.Cart.CartReqDTO;
import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Enums.CartStatus;

import java.util.List;

public interface CartService {
    Cart createCart(User user);
    List<CartDTO> getCartForCurrentUser();
    CartDTO addItem(CartReqDTO cartReqDTO);
    CartDTO updateItemQuantity(Integer cartItemId, Integer newQuantity);
    void removeItem(Integer cartItemId);

    void clearCartItem();
}
