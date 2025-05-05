package com.example.project_shopping.Service;

import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.User;

public interface CartService {
    Cart createCart(User user);
}
