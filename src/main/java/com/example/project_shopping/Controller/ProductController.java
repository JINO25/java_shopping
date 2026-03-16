package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Product.*;
import com.example.project_shopping.Service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<ProductListDTO>> getAllProduct(@RequestParam(value = "name", required = false) String name){
        List<ProductListDTO> productDTO;
        if(name != null && !name.isEmpty()){
            productDTO = productService.findProductByName(name);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        }
        productDTO = productService.findAll();
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductByID(@PathVariable Integer id){
        ProductDetailDTO productDTO = productService.findProductByID(id);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductListDTO>> getProductByCategory(@PathVariable String category){
        List<ProductListDTO> productDTO = productService.findProductByCategory(category);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid CreateProductRequestDTO dto) {
        ProductDTO result = productService.create(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}/product-variant/{variantID}")
    public ResponseEntity<ProductDTO> updateProduct(
            @RequestBody @Valid UpdateProductReqDTO dto,
            @PathVariable("id") Integer id,
            @PathVariable("variantID")  Integer variantId ) {
        ProductDTO result = productService.update(id,variantId,dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}/multi-variants")
    public ResponseEntity<ProductDTO> updateProductWithMultiVariants(
            @RequestBody @Valid ProductDTO dto,
            @PathVariable("id") Integer id) {
        ProductDTO result = productService.updateMultiVariants(id, dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}/product-variant/{variantID}")
    public ResponseEntity<?> deleteVariant(
            @PathVariable("id") Integer id,
            @PathVariable("variantID") Integer variantId
    ){
        boolean status = productService.deleteVariant(id, variantId);
        if(!status){
            return new ResponseEntity<>("delete unsuccessful!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("delete successful!",HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}/product")
    public ResponseEntity<?> deleteProduct(
            @PathVariable("id") Integer id
    ){
        boolean status = productService.deleteProduct(id);
        if(!status){
            return new ResponseEntity<>("delete unsuccessful!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("delete successful!",HttpStatus.OK);
    }
}
