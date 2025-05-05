package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Product.CreateProductRequestDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.UpdateProductReqDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();

    ProductDTO findProductByID(Integer id);

    List<ProductDTO> findProductByCategory(String name);

    List<ProductDTO> findProductByName(String name);

    ProductDTO create(CreateProductRequestDTO productDTO);

    ProductDTO update(Integer id, Integer variantId , UpdateProductReqDTO updateProductReqDTO);
    ProductDTO updateMultiVariants(Integer id, ProductDTO updateProductReqDTO);

    boolean deleteVariant(Integer id, Integer variantID);
    boolean deleteProduct(Integer id);


}
