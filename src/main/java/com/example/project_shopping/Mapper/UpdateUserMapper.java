package com.example.project_shopping.Mapper;

import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.Entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateUserMapper {

    private final ModelMapper modelMapper;

    public UpdateUserDTO toDTO(User user) {
        return modelMapper.map(user, UpdateUserDTO.class);
    }

    public void updateEntity(UpdateUserDTO updateUserDTO, User existingUser) {
        modelMapper.map(updateUserDTO, existingUser);
    }
}
