package com.example.project_shopping.Service;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.DeleteUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.User;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO createUser(CreateUserDTO createUserDTO);
    UserResponseDTO createAdmin(CreateUserDTO createUserDTO);
    UserResponseDTO createSeller(CreateUserDTO createUserDTO);


    UserResponseDTO findById(Integer id);

    UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO);
    void deleteUser(Integer id);

}
