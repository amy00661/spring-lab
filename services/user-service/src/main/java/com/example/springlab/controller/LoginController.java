package com.example.springlab.controller;

import com.example.springlab.component.JwtTokenProvider;
import com.example.springlab.controller.request.LoginRequest;
import com.example.springlab.model.UserEntity;
import com.example.springlab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Operation(summary = "登入")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 驗證帳號密碼
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成 JWT（改：帶入 authentication 以包含使用者名稱/權限）
        String token = jwtTokenProvider.generateToken(authentication);

        Map<String, Object> response = new HashMap<>();
        response.put("username", request.getUsername());
        response.put("token", token);
        response.put("tokenType", "Bearer");
        return ResponseEntity.ok(response);
    }
}