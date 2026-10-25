package com.auth.authservice.controller;

import com.auth.authservice.dto.UserDto;
import com.auth.authservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable String userId) throws Exception {
        userService.deleteUser(userId);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getByEmail(@PathVariable String email) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmail(email));
    }
}
