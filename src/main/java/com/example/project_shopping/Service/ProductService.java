package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Product.*;

import java.util.List;

public interface ProductService {
    List<ProductListDTO> findAll();

    ProductDetailDTO findProductByID(Integer id);

    List<ProductListDTO> findProductByCategory(String name);

    List<ProductListDTO> findProductByName(String name);

    ProductDTO create(CreateProductRequestDTO productDTO);

    ProductDTO update(Integer id, Integer variantId , UpdateProductReqDTO updateProductReqDTO);
//    ProductDTO update(Integer productId, UpdateProductReqDTO dto);
    ProductDTO updateMultiVariants(Integer id, ProductDTO updateProductReqDTO);

    boolean deleteVariant(Integer id, Integer variantID);
    boolean deleteProduct(Integer id);


}
