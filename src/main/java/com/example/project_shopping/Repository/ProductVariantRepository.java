package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Integer> {
}
