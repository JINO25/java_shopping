package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Cart;
import com.example.project_shopping.Entity.CartItem;
import com.example.project_shopping.Enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cart AND ci.status = :status")
    void cleanItem(@Param("cart") Integer cart, @Param("status") CartStatus status);

}
