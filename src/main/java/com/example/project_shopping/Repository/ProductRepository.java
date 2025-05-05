package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findProductByCategory(Category category);

    @Query("SELECT p FROM Product p " +
            "JOIN p.productVariants " +
            "WHERE LOWER(p.name) LIKE concat('%',:name,'%')")
    List<Product> findProductByNameLike(@Param("name") String name);

}
