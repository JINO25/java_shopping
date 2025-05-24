package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Image.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageDTO> getAllImages();
    ImageDTO getImageById(Integer id);
    List<ImageDTO> saveImages(Integer productId, List<MultipartFile> multipartFiles);
    ImageDTO updateImage(Integer imageId, Integer productId, MultipartFile multipartFiles);
    void deleteImage(Integer id);
}

