package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name",target = "name")
    CategoryDTO toResponseDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}
