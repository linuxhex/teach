package com.teacher.controller;

import com.teacher.dto.ApiResponse;
import com.teacher.dto.LoginRequest;
import com.teacher.dto.RegisterRequest;
import com.teacher.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest req) {
        try {
            return ApiResponse.ok(authService.login(req));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @GetMapping("/profile")
    public ApiResponse<?> profile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.ok(authService.getProfile(userId));
    }
}
