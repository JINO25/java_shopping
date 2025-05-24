package com.example.project_shopping.DTO.Image;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDTO {
    private Integer id;
    private String name;
    private String url;
    private String publicId;
    private Integer productId;

}
