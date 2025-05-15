package com.example.project_shopping.Service.Imp;

import com.example.project_shopping.DTO.User.CreateUserDTO;
import com.example.project_shopping.DTO.User.UpdateUserDTO;
import com.example.project_shopping.DTO.User.UserResponseDTO;
import com.example.project_shopping.Entity.Address;
import com.example.project_shopping.Entity.Role;
import com.example.project_shopping.Entity.User;
import com.example.project_shopping.Exception.EmailAlreadyExistsException;
import com.example.project_shopping.Exception.EntityNotFoundException;
import com.example.project_shopping.Mapper.UpdateUserMapper;
import com.example.project_shopping.Mapper.UserMapper;
import com.example.project_shopping.Repository.AddressRepository;
import com.example.project_shopping.Repository.RoleRepository;
import com.example.project_shopping.Repository.UserRepository;
import com.example.project_shopping.Service.CartService;
import com.example.project_shopping.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AddressRepository addressRepository;
    private CartService cartService;
    private UserMapper userMapper;
    private UpdateUserMapper updateUserMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> findAllUsers() {
            List<User> users = userRepository.findAll();
            List<UserResponseDTO> userResponseDTOs = users.stream()
                    .map(userMapper::toResponseDTO)
                    .collect(Collectors.toList());

            return userResponseDTOs;
    }

    @Override
    public UserResponseDTO findById(Integer id){
        UserResponseDTO userResponseDTO = null;
        User user = userRepository.findUserById(id);
        if(user==null) throw new EntityNotFoundException("User not found with "+id);
        userResponseDTO = userMapper.toResponseDTO(user);
        return userResponseDTO;
    }

    @Override
    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
           User user = userMapper.toEntity(createUserDTO);
           Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
           if(existingUser.isPresent()) {
               System.out.println("Email already exists: " + user.getEmail());
               throw new EmailAlreadyExistsException("Email already exists");
           }

           Role defaultRole = roleRepository.findByRole("ROLE_USER");
           if (defaultRole == null) {
               defaultRole = new Role();
               defaultRole.setRole("ROLE_USER");
               roleRepository.save(defaultRole);
           }
           user.setRole(defaultRole);
           user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

           user = userRepository.save(user);

           Address address = new Address();
           address.setCity(createUserDTO.getCity());
           address.setCountry(createUserDTO.getCountry());
           address.setStreet(createUserDTO.getStreet());
           address.setPhoneNumber(createUserDTO.getPhoneNumber());
           address.setUser(user);

           if(user.getAddresses() == null){
               user.setAddresses(new LinkedHashSet<>());
           }

           user.getAddresses().add(address);
           addressRepository.save(address);
           cartService.createCart(user);

           return userMapper.toResponseDTO(user);
    }

    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDTO) {
        User user = userRepository.findUserByEmail(updateUserDTO.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("No user found with email: " + updateUserDTO.getEmail()));

        boolean isUpdated = false;

        if (updateUserDTO.getName() != null && !updateUserDTO.getName().equals(user.getName())) {
            user.setName(updateUserDTO.getName());
            isUpdated = true;
        }

        if (updateUserDTO.getPhoto() != null && !updateUserDTO.getPhoto().equals(user.getPhoto())) {
            user.setPhoto(updateUserDTO.getPhoto());
            isUpdated = true;
        }

        if (isUpdated) {
            user = userRepository.save(user);
        }

        return updateUserMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        userRepository.delete(user);
    }

}
