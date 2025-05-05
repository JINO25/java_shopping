package com.example.project_shopping.Repository;

import com.example.project_shopping.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findCategoryById(Integer id);
    Optional<Category> findCategoryByName(String name);

    Category findByName(String categoryName);
}
