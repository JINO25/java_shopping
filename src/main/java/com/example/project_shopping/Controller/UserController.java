package com.example.project_shopping.Controller;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.DeleteUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Exception.EmailAlreadyExistsException;
import com.example.project_shopping.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public  ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        userResponseDTOList=userService.findAllUsers();
        return new ResponseEntity<>(userResponseDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Integer id){
        UserResponseDTO userResponseDTO = userService.findById(id);
        return new ResponseEntity<>(userResponseDTO,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            bindingResult.getAllErrors().forEach(e->{
                errorMsg.append(e.getDefaultMessage()).append("\n");
            });
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        UserResponseDTO response = userService.createUser(createUserDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PatchMapping
    public ResponseEntity<UpdateUserDTO> updateUserDTOResponseEntity(@RequestBody UpdateUserDTO updateUserDTO){
        UpdateUserDTO updateUser = userService.updateUser(updateUserDTO);
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
    }


}
