package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Cart.CartDTO;
import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Mapper.CartMapper;
import com.example.project_shopping.Repository.CartRepository;
import com.example.project_shopping.Service.CartService;
import com.example.project_shopping.Util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImp implements CartService {
    private CartMapper cartMapper;
    private CartRepository cartRepository;
    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getCartForCurrentUser() {
        Integer userId = Auth.getCurrentUserID();
        List<Cart> cartList = cartRepository.findByUserId(userId);
        if(cartList.isEmpty()){
            return Collections.emptyList();
        }
        return cartMapper.toCartDTOList(cartList);
    }
}
