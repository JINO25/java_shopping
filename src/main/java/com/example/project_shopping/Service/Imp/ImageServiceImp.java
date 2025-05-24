package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.Cloudinary.CloudinaryImageUploadResultDTO;
import com.example.project_shopping.DTO.Image.ImageDTO;
import com.example.project_shopping.Entity.Image;
import com.example.project_shopping.Entity.Product;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Repository.ImageRepository;
import com.example.project_shopping.Repository.ProductRepository;
import com.example.project_shopping.Service.CloudinaryService;
import com.example.project_shopping.Service.ImageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImageServiceImp implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Override
    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ImageDTO getImageById(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id " + id));
        return toDTO(image);
    }

    @Override
    public List<ImageDTO> saveImages(Integer productId, List<MultipartFile> multipartFiles) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        List<Image> savedImages = new ArrayList<>();

        for(MultipartFile file : multipartFiles){
        CloudinaryImageUploadResultDTO resultDTO = cloudinaryService.uploadImage(file);
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setUrl(resultDTO.getUrl());
        image.setPublicId(resultDTO.getPublicId());
        image.setProduct(product);
        savedImages.add(image);
        }

        savedImages = imageRepository.saveAll(savedImages);
        return savedImages.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ImageDTO updateImage(Integer imageId,Integer productId, MultipartFile multipartFile) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));


        if (!image.getProduct().getId().equals(product.getId())) {
            throw new EntityNotFoundException("Product and Image do not match!");
        }

        CloudinaryImageUploadResultDTO resultDTO = cloudinaryService.uploadImage(multipartFile);
        image.setName(multipartFile.getOriginalFilename());
        image.setUrl(resultDTO.getUrl());
        image.setPublicId(resultDTO.getPublicId());
        image.setProduct(product);

        return toDTO(imageRepository.save(image));
    }

    @Override
    public void deleteImage(Integer id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Image not found!"));

        cloudinaryService.deleteImage(image.getPublicId());

        imageRepository.deleteById(id);
    }

    private ImageDTO toDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setId(image.getId());
        dto.setName(image.getName());
        dto.setUrl(image.getUrl());
        dto.setPublicId(image.getPublicId());
        dto.setProductId(image.getProduct().getId());
        return dto;
    }
}
