package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.DTO.Category.CategoryWithProductListDTO;
import com.example.project_shopping.Entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO create(CategoryDTO categoryDTO);
    CategoryWithProductListDTO findCategoryWithProduct(Integer id);
    CategoryDTO update(CategoryDTO categoryDTO,Integer id);
    boolean delete(Integer id);

}
