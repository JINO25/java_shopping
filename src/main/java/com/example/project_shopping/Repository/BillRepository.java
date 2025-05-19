package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Bill;
import com.example.project_shopping.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("select distinct b " +
            "from Bill b " +
            "join b.order o " +
            "where o.user.id  = :userID")
    List<Bill> findBillsByUserID(@Param("userID") Integer userID);


    @Query("SELECT DISTINCT b " +
            "FROM Bill b " +
            "JOIN b.order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.productVariant pv " +
            "JOIN pv.product p " +
            "WHERE p.user.id = :sellerId")
    List<Bill> findBillsBySellerId(@Param("sellerId") Integer sellerId);

    Bill findBillsByOrder(Order order);
}
