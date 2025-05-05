package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Category.CategoryDTO;
import com.example.project_shopping.DTO.Category.CategoryWithProductListDTO;
import com.example.project_shopping.Service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryWithProductListDTO> getCategoryWithProducts(@PathVariable Integer id) {
        CategoryWithProductListDTO categoryDTO = categoryService.findCategoryWithProduct(id);
        if (categoryDTO != null) {
            return ResponseEntity.ok(categoryDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.create(categoryDTO);
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable Integer id){
        CategoryDTO category = categoryService.update(categoryDTO, id);
        if(category == null){
            return new ResponseEntity<>("Update unsuccessful!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id){
        boolean check = categoryService.delete(id);
        if(check == false){
            return new ResponseEntity<>("Delete unsuccessful!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Delete successful");
    }
}
