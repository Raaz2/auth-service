package com.auth.authservice.service;

import com.auth.authservice.dto.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
