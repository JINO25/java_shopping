package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Entity.ProductVariant;
import com.example.project_shopping.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name",source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "productVariants", source = "productVariants")
    ProductDTO toProductDTO(Product product);


    Product toProduct(ProductDTO productDTO);

    ProductVariantDTO toProductVariantDTO(Product product);

    ProductVariant toProductVariant(ProductVariantDTO productVariantDTO);

    List<ProductDTO> toProductDTOList(List<Product> products);


    List<ProductVariantDTO> toProductVariantDTOList(List<ProductVariantDTO> productVariants);
}
