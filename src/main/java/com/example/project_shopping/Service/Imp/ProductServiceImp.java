package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Product.*;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImp implements ProductService {
    private ProductRepository productRepository;
    private ProductVariantRepository productVariantRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Override
    public List<ProductListDTO> findAll() {
        List<Product> products = productRepository.findAllWithImagesAndVariants();
        return productMapper.toProductListDTOs(products);
    }


    @Override
    public ProductDetailDTO findProductByID(Integer id){
        Product product = productRepository.findByIdWithDetails(id).orElseThrow(()->new EntityNotFoundException("Product not found with id: "+id));
        return productMapper.toProductDetailDTO(product);
    }

    @Override
    public List<ProductListDTO> findProductByCategory(String name){
        Category category = categoryRepository.findByName(name);

        if(category == null) throw new EntityNotFoundException("Category: "+ name+" not found!");

        List<Product> product = productRepository.findProductByCategory(category);

        return productMapper.toProductListDTOs(product);

    }

    @Override
    public List<ProductListDTO> findProductByName(String name){
        List<Product> product = productRepository.findProductByNameLike(name);
        return productMapper.toProductListDTOs(product);
    }

//    @Override
//    public ProductDTO create(CreateProductRequestDTO dto) {
//        Integer userID = Auth.getCurrentUserID();
//        Product product = new Product();
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//
//        Category category = categoryRepository.findByName(dto.getCategoryName());
//        if (category == null) {
//            category = new Category();
//            category.setName(dto.getCategoryName());
//            categoryRepository.save(category);
//        }
//        product.setCategory(category);
//
//        User user = userRepository.findById(userID)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        product.setUser(user);
//
//
//        Set<ProductVariant> variants = new HashSet<>();
//        for (ProductVariantDTO vDto : dto.()) {
//            ProductVariant v = new ProductVariant();
//            v.setOption(vDto.getOption());
//            v.setColor(vDto.getColor());
//            v.setPrice(vDto.getPrice());
//            v.setStock(vDto.getStock());
//            v.setProduct(product); // quan hệ ngược
//
//            variants.add(v);
//        }
//
//        product.setProductVariants(variants);
//
//        Product savedProduct = productRepository.save(product);
//
//        return productMapper.toProductDTO(savedProduct);
//    }

    @Override
    public ProductDTO create(CreateProductRequestDTO dto) {
        Integer userID = Auth.getCurrentUserID();

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());

        // Category
        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(dto.getCategoryName());
            categoryRepository.save(category);
        }
        product.setCategory(category);

        // User
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        product.setUser(user);

        // Variants
        Set<ProductVariant> variants = new HashSet<>();
        for (ProductVariantDTO vDto : dto.getVariants()) {
            ProductVariant v = new ProductVariant();
            v.setOption(vDto.getOption());
            v.setColor(vDto.getColor());
            v.setPrice(vDto.getPrice());
            v.setStock(vDto.getStock());
            v.setProduct(product); // quan hệ ngược

            variants.add(v);
        }

        product.setProductVariants(variants);

        Product savedProduct = productRepository.save(product);

        return productMapper.toProductDTO(savedProduct);
    }


    @Override
    public ProductDTO update(Integer productId, Integer variantId, UpdateProductReqDTO dto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());

        Category category = categoryRepository.findByName(dto.getCategoryName());
        if (category == null) {
            category = new Category();
            category.setName(dto.getCategoryName());
            categoryRepository.save(category);
        }
        product.setCategory(category);

        ProductVariant variant = product.getProductVariants().stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Variant not found with id: " + variantId + " in product " + productId
                ));

        // Update variant
        variant.setOption(dto.getOption());
        variant.setColor(dto.getColor());
        variant.setPrice(dto.getPrice());
        variant.setStock(dto.getStock());

        // Không cần save variant riêng nếu cascade = ALL
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductDTO(savedProduct);
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
