package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.Image.ImageDTO;
import com.example.project_shopping.Service.ImageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Integer id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<ImageDTO>> uploadImages(@RequestParam("productId") Integer productId,
                                                       @RequestParam("files") List<MultipartFile> files) {
        List<ImageDTO> savedImages = imageService.saveImages(productId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImages);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Integer id,
                                                @RequestParam("productId") Integer productId,
                                                @RequestParam("files") MultipartFile file) {
        ImageDTO updated = imageService.updateImage(id, productId, file);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}

