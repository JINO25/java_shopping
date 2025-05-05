package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.DTO.Category.CategoryWithProductListDTO;
import com.example.project_shopping.DTO.Product.ProductDTO;
import com.example.project_shopping.Entity.Category;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Mapper.CategoryMapper;
import com.example.project_shopping.Mapper.CategoryWithProductMapper;
import com.example.project_shopping.Repository.CategoryRepository;
import com.example.project_shopping.Repository.ProductRepository;
import com.example.project_shopping.Service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private CategoryWithProductMapper categoryWithProductMapper;
    private final ProductRepository productRepository;

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    public CategoryWithProductListDTO findCategoryWithProduct(Integer id) {
        Category category = categoryRepository.findCategoryById(id);
        if (category != null) {
            List<Product> productList = productRepository.findProductByCategory(category);
//            List<ProductDTO> productDTOList = categoryWithProductMapper.toProductDTOList(productList);
            CategoryWithProductListDTO categoryWithProductListDTO = categoryWithProductMapper.toResponseDTO(category);
//            categoryWithProductListDTO.setProductList(productDTOList);
            categoryWithProductListDTO.setProductList(productList);
            return categoryWithProductListDTO;
        }

        return null;
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, Integer id) {
        Category category = categoryMapper.toEntity(categoryDTO);
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()){
            Category newCategory = categoryOptional.get();
            newCategory.setName(categoryDTO.getName());
            newCategory = categoryRepository.save(newCategory);
            return categoryMapper.toResponseDTO(newCategory);
        }

        return null;
    }

    @Override
    public boolean delete(Integer id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
