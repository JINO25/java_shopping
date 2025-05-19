package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Order;
import com.example.project_shopping.Entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    @Query("SELECT DISTINCT o " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.productVariant pv " +
            "WHERE pv.product.user.id = :sellerId")
    List<Order> findOrdersBySellerId(@Param("sellerId") Integer sellerId);

}
