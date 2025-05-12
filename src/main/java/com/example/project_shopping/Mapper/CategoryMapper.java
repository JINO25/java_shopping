package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.Entity.Category;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryMapper {
    private ModelMapper modelMapper;

    public CategoryDTO toResponseDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public Category toEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }
}
