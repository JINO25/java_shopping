package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Entity.ProductVariant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductMapper {

    private final ModelMapper modelMapper;

    public ProductDTO toProductDTO(Product product) {
        // Custom mapping userName và categoryName
        modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getUser().getName(), ProductDTO::setUserName);
            mapper.map(src -> src.getCategory().getName(), ProductDTO::setCategoryName);
        });

        return modelMapper.map(product, ProductDTO.class);
    }

    public Product toProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    public ProductVariantDTO toProductVariantDTO(ProductVariant productVariant) {
        return modelMapper.map(productVariant, ProductVariantDTO.class);
    }

    public ProductVariant toProductVariant(ProductVariantDTO productVariantDTO) {
        return modelMapper.map(productVariantDTO, ProductVariant.class);
    }

    public List<ProductDTO> toProductDTOList(List<Product> products) {
        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }

    public List<ProductVariantDTO> toProductVariantDTOList(List<ProductVariant> productVariants) {
        return productVariants.stream()
                .map(this::toProductVariantDTO)
                .collect(Collectors.toList());
    }
}
