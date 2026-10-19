package com.auth.authservice.service;

import com.auth.authservice.dto.UserDto;

public interface UserService {
    //create user
    UserDto createUser(UserDto userDto);

    //get user by email
    UserDto getUserByEmail(String email) throws Exception;

    //update user
    UserDto updateUser(UserDto userDto, String userId) throws Exception;

    //delete user
    void deleteUser(String userId) throws Exception;

    //get user by id
    UserDto getUserById(String userId) throws Exception;

    //get all users
    Iterable<UserDto> getAllUsers();

    // user service se related __

}
