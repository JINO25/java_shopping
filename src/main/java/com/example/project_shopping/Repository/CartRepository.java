package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);
    Cart findCartByUser(User user);
}
