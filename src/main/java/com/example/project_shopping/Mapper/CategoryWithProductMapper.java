package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.DTO.Category.CategoryWithProductListDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryWithProductMapper {
    CategoryWithProductMapper INSTANCE = Mappers.getMapper(CategoryWithProductMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name",target = "name")
    @Mapping(target = "productList", source = "products")
    CategoryWithProductListDTO toResponseDTO(Category category);

    Category toEntity(CategoryWithProductListDTO categoryWithProductListDTO);

//    List<ProductDTO> toProductDTOList(List<Product> products);
}
