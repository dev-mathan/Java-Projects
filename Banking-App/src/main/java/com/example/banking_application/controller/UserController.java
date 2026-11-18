package com.example.banking_application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banking_application.entity.User;
import com.example.banking_application.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{uid}")
    public ResponseEntity<User> getUser(@PathVariable Long uid) {
        return ResponseEntity.ok(userService.getUserById(uid));
    }

    @PutMapping("/{uid}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long uid,
            @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(uid, user));
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long uid) {
        userService.deleteUser(uid);
        return ResponseEntity.noContent().build();
    }
}
