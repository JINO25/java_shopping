package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UpdateUserMapper {
    UpdateUserMapper INSTANCE = Mappers.getMapper(UpdateUserMapper.class);

    UpdateUserDTO toResponseDTO(User user);

    User toEntity(UpdateUserDTO updateUserDTO);
}
