package com.auth.authservice.service.impl;

import com.auth.authservice.dto.UserDto;
import com.auth.authservice.model.Provider;
import com.auth.authservice.model.User;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // final fields get autowired by default with constructor injection
    private final ModelMapper modelMapper;
    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given email already exists");
        }
        // if you have extra checks __put here...
        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
        //role assign here to user___for authorization

        //TODO:
        //assign the default role

        Role role = roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE).orElse(null);
        user.getRoles().add(role);




        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return null;
    }
}
