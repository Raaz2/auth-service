package com.auth.authservice.service.impl;

import com.auth.authservice.dto.UserDto;
import com.auth.authservice.service.AuthService;
import com.auth.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
