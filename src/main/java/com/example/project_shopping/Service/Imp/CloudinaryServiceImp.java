package com.example.project_shopping.Service.Imp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.project_shopping.DTO.Cloudinary.CloudinaryImageUploadResultDTO;
import com.example.project_shopping.Service.CloudinaryService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImp implements CloudinaryService {
    @Value("${CLOUDINARY_NAME}")
    private String cloudName;
    @Value("${CLOUDINARY_API_KEY}")
    private String apiKey;
    @Value("${CLOUDINARY_SECRET_KEY}")
    private String secretKey;

    private Cloudinary cloudinary;

    @PostConstruct
    private void init() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", secretKey);
        this.cloudinary = new Cloudinary(config);
    }

    @Override
    public CloudinaryImageUploadResultDTO uploadImage(MultipartFile multipartFile) {
        try {
            Map data = this.cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.asMap("folder","java"));
            CloudinaryImageUploadResultDTO resultDTO = new CloudinaryImageUploadResultDTO();
            resultDTO.setUrl(data.get("secure_url").toString());
            resultDTO.setPublicId(data.get("public_id").toString());
            return resultDTO;
        }catch (IOException io){
            io.printStackTrace();
            throw new RuntimeException("Image upload fail!");
        }
    }

    @Override
    public void deleteImage(String publicID) {
        try {
            cloudinary.uploader().destroy(publicID, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from Cloudinary", e);
        }
    }
}
