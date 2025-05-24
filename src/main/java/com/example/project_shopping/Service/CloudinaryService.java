package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.Cloudinary.CloudinaryImageUploadResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudinaryImageUploadResultDTO uploadImage(MultipartFile multipartFile);
    void deleteImage(String publicID);
}
