package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Product.CreateProductRequestDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.DTO.Product.ProductVariantDTO;
import com.example.project_shopping.DTO.Product.UpdateProductReqDTO;
import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Entity.ProductVariant;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Mapper.ProductMapper;
import com.example.project_shopping.Repository.CategoryRepository;
import com.example.project_shopping.Repository.ProductRepository;
import com.example.project_shopping.Repository.ProductVariantRepository;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.ProductService;
import com.example.project_shopping.Util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductServiceImp implements ProductService {
    private ProductRepository productRepository;
    private ProductVariantRepository productVariantRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOs = productMapper.toProductDTOList(products);

        return productDTOs;
    }

    @Override
    public ProductDTO findProductByID(Integer id){
        Product product = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id));
        return productMapper.toProductDTO(product);
    }

    @Override
    public List<ProductDTO> findProductByCategory(String name){
        Category category = categoryRepository.findByName(name);
        if(category == null) throw new EntityNotFoundException("Category: "+ name+" not found!");
        List<Product> product = productRepository.findProductByCategory(category);
        System.out.println("Found products: " + product.size());
        return productMapper.toProductDTOList(product);

    }

    @Override
    public List<ProductDTO> findProductByName(String name){
        List<Product> product = productRepository.findProductByNameLike(name);
        return productMapper.toProductDTOList(product);
    }

    @Override
    public ProductDTO create(CreateProductRequestDTO dto) {
        Integer userID = Auth.getCurrentUserID();
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());

        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(dto.getCategoryName());
            categoryRepository.save(category);
        }
        product.setCategory(category);

        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        product.setUser(user);


        ProductVariant variant = new ProductVariant();
        variant.setOption(dto.getOption());
        variant.setColor(dto.getColor());
        variant.setPrice(dto.getPrice());
        variant.setStock(dto.getStock());
        variant.setProduct(product);

        product.setProductVariants(new HashSet<>(List.of(variant)));
        productRepository.save(product);

        return productMapper.toProductDTO(product);
    }

    @Override
    public ProductDTO update(Integer id, Integer variantId, UpdateProductReqDTO updateProductReqDTO){
        Product existingProduct = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id));

        existingProduct.setName(updateProductReqDTO.getName());
        existingProduct.setDescription(updateProductReqDTO.getDescription());

        Category category = categoryRepository.findByName(updateProductReqDTO.getCategoryName());
        if(category == null){
            category = new Category();
            category.setName(updateProductReqDTO.getCategoryName());
            categoryRepository.save(category);
        }

        existingProduct.setCategory(category);

        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(()->new RuntimeException("Product variant not found with id: "+variantId));
        variant.setOption(updateProductReqDTO.getOption());
        variant.setColor(updateProductReqDTO.getColor());
        variant.setPrice(updateProductReqDTO.getPrice());
        variant.setStock(updateProductReqDTO.getStock());
        variant.setProduct(existingProduct);
        productVariantRepository.save(variant);
        productRepository.save(existingProduct);

        return  productMapper.toProductDTO(existingProduct);
    }

    @Override
    public ProductDTO updateMultiVariants(Integer productId, ProductDTO updateProductReqDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        product.setName(updateProductReqDTO.getName());
        product.setDescription(updateProductReqDTO.getDescription());

        // Category
        Category category = categoryRepository.findByName(updateProductReqDTO.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(updateProductReqDTO.getCategoryName());
            categoryRepository.save(category);
        }
        product.setCategory(category);

        // Xử lý variants
        Set<ProductVariant> variantSet = new HashSet<>();

        if (updateProductReqDTO.getProductVariants() != null) {
            for (ProductVariantDTO variantDTO : updateProductReqDTO.getProductVariants()) {
                ProductVariant variant;

                if (variantDTO.getId() != null) {
                    // Nếu đã có ID -> update
                    variant = productVariantRepository.findById(variantDTO.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Variant not found with id: " + variantDTO.getId()));
                } else {
                    // Nếu không có ID -> tạo mới
                    variant = new ProductVariant();
                    variant.setProduct(product);
                }

                variant.setOption(variantDTO.getOption());
                variant.setColor(variantDTO.getColor());
                variant.setPrice(variantDTO.getPrice());
                variant.setStock(variantDTO.getStock());

                productVariantRepository.save(variant); // lưu từng cái
//                variantSet.add(variant);
            }

//            product.setProductVariants(variantSet);
        }

        productRepository.save(product);

        return productMapper.toProductDTO(product);
    }

    @Override
    public boolean deleteVariant(Integer id, Integer variantID){
        Product existingProduct = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id));
        ProductVariant productVariant = productVariantRepository.findById(variantID).orElseThrow(()->new EntityNotFoundException("Product variant not found with id: "+variantID));
        productVariantRepository.delete(productVariant);
        return true;
    }

    @Override
    public boolean deleteProduct(Integer id){
        Product existingProduct = productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id));
        productRepository.delete(existingProduct);
        return true;
    }
}
