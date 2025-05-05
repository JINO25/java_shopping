package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "role.role",target = "roleName")
    UserResponseDTO toResponseDTO(User user);


//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "email", target = "email")
//    @Mapping(source = "password", target = "password")
//    @Mapping(source = "address", target = "address")
//    @Mapping(source = "photo", target = "photo")
    User toEntity(CreateUserDTO createUserDTO);
}
