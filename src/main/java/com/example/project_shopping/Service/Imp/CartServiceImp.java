package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Repository.CartRepository;
import com.example.project_shopping.Service.CartService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartServiceImp implements CartService {
    private CartRepository cartRepository;
    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cartRepository.save(cart);
    }
}
