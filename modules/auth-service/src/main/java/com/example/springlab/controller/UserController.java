package com.example.springlab.controller;

import com.example.springlab.model.UserEntity;
import com.example.springlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users 取得所有使用者
    @Operation(summary = "取得所有使用者")
    @GetMapping("/getAll")
    public List<UserEntity> getAllUsers() {
        return userService.findAllUsers();
    }
}