package com.example.banking_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.banking_application.entity.User;
import com.example.banking_application.exception.ResourceNotFountException;
import com.example.banking_application.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                    "Email already exists");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new IllegalArgumentException(
                    "Phone already exists");
        }

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long uid) {
        return userRepository.findById(uid)
                .orElseThrow(() ->
                    new ResourceNotFountException(
                        "User not found: " + uid));
    }

    public User updateUser(Long uid, User updatedUser) {
        User user = getUserById(uid);

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }

    public void deleteUser(Long uid) {
        User user = getUserById(uid);
        userRepository.delete(user);
    }
}
