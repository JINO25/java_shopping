package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.Image.ImageDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.ProductDetailDTO;
import com.example.project_shopping.DTO.Product.ProductListDTO;
import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Entity.ProductVariant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Component
//@AllArgsConstructor
//public class ProductMapper {
//
//    private final ModelMapper modelMapper;
//
//    public ProductDTO toProductDTO(Product product) {
//        // Custom mapping userName và categoryName
//        modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
//            mapper.map(src -> src.getUser().getName(), ProductDTO::setUserName);
//            mapper.map(src -> src.getCategory().getName(), ProductDTO::setCategoryName);
//        });
//
//        return modelMapper.map(product, ProductDTO.class);
//    }
//
//    public Product toProduct(ProductDTO productDTO) {
//        return modelMapper.map(productDTO, Product.class);
//    }
//
//    public ProductVariantDTO toProductVariantDTO(ProductVariant productVariant) {
//        return modelMapper.map(productVariant, ProductVariantDTO.class);
//    }
//
//    public ProductVariant toProductVariant(ProductVariantDTO productVariantDTO) {
//        return modelMapper.map(productVariantDTO, ProductVariant.class);
//    }
//
//    public List<ProductDTO> toProductDTOList(List<Product> products) {
//        return products.stream()
//                .map(this::toProductDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<ProductVariantDTO> toProductVariantDTOList(List<ProductVariant> productVariants) {
//        return productVariants.stream()
//                .map(this::toProductVariantDTO)
//                .collect(Collectors.toList());
//    }
//}
@Component
public class ProductMapper {

    public ProductListDTO toProductListDTO(Product p) {
        ProductListDTO dto = new ProductListDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setCategoryName(p.getCategory().getName());

        if (!p.getImages().isEmpty()) {
            dto.setThumbnail(p.getImages().stream().findFirst().map(img -> img.getUrl()).orElse(""));
        }

        Double min = p.getProductVariants().stream()
                .map(ProductVariant::getPrice)
                .min(Double::compareTo)
                .orElse(0.0);

        Double max = p.getProductVariants().stream()
                .map(ProductVariant::getPrice)
                .max(Double::compareTo)
                .orElse(0.0);

        dto.setMinPrice(min);
        dto.setMaxPrice(max);

        return dto;
    }

    public ProductDetailDTO toProductDetailDTO(Product p) {
        ProductDetailDTO dto = new ProductDetailDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setSellerName(p.getUser().getName());
        dto.setCategoryName(p.getCategory().getName());

        dto.setImages(
                p.getImages().stream()
                        .map(img -> img.getUrl())
                        .toList()
        );

        dto.setVariants(
                p.getProductVariants().stream()
                        .map(this::toVariantDTO)
                        .toList()
        );

        return dto;
    }

    public ProductVariantDTO toVariantDTO(ProductVariant v) {
        ProductVariantDTO dto = new ProductVariantDTO();
        dto.setId(v.getId());
        dto.setColor(v.getColor());
        dto.setOption(v.getOption());
        dto.setPrice(v.getPrice());
        dto.setStock(v.getStock());
        return dto;
    }

    public List<ProductListDTO> toProductListDTOs(List<Product> products) {
        return products.stream().map(this::toProductListDTO).toList();
    }
    public ProductDTO toProductDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setUserName(p.getUser().getName());
        dto.setCategoryName(p.getCategory().getName());

        // Variants
        dto.setProductVariants(
                p.getProductVariants().stream()
                        .map(this::toVariantDTO)
                        .toList()
        );

        // Images (Set -> List<ImageDTO>)
        dto.setImages(
                p.getImages().stream()
                        .map(image -> {
                            ImageDTO imageDTO = new ImageDTO();
                            imageDTO.setId(image.getId());
                            imageDTO.setUrl(image.getUrl());
                            return imageDTO;
                        })
                        .toList()
        );

        return dto;
    }


}
