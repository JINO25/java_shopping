package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
