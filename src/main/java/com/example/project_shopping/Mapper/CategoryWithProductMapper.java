package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Category.CategoryWithProductListDTO;
import com.example.project_shopping.Entity.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryWithProductMapper {
    private ModelMapper modelMapper;

    public CategoryWithProductListDTO toResponseDTO(Category category) {
        return modelMapper.map(category, CategoryWithProductListDTO.class);
    }

    public Category toEntity(CategoryWithProductListDTO categoryWithProductListDTO) {
        return modelMapper.map(categoryWithProductListDTO, Category.class);
    }
}
