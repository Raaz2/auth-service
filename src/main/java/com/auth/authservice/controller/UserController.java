package com.auth.authservice.controller;

import com.auth.authservice.dto.UserDto;
import com.auth.authservice.model.User;
import com.auth.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }


    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
