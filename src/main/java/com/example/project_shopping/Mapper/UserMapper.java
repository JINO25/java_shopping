package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private ModelMapper modelMapper;

    public UserResponseDTO toResponseDTO(User user) {
        // Mapping thủ công cho roleName
        modelMapper.typeMap(User.class, UserResponseDTO.class).addMappings(mapper ->
                mapper.map(src -> src.getRole().getRole(), UserResponseDTO::setRoleName)
        );
        return modelMapper.map(user, UserResponseDTO.class);
    }

    public User toEntity(CreateUserDTO createUserDTO) {
        return modelMapper.map(createUserDTO, User.class);
    }

    public User updateEntity(UpdateUserDTO updateUserDTO, User existingUser) {
        modelMapper.map(updateUserDTO, existingUser);
        return existingUser;
    }

}
