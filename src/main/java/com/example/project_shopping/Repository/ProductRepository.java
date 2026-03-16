package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
    select p from Product p
    left join fetch p.images i
    left join p.productVariants v
""")
    List<Product> findAllWithImagesAndVariants();

    @Query("""
    select p from Product p
    left join fetch p.images
    left join fetch p.productVariants
    left join fetch p.category
    left join fetch p.user
    where p.id = :id
""")
    Optional<Product> findByIdWithDetails(Integer id);


    List<Product> findProductByCategory(Category category);

    @Query("SELECT p FROM Product p " +
            "JOIN p.productVariants " +
            "WHERE LOWER(p.name) LIKE concat('%',:name,'%')")
    List<Product> findProductByNameLike(@Param("name") String name);

}
