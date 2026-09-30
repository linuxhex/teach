package com.teacher.service;

import com.teacher.config.JwtUtil;
import com.teacher.dto.ApiResponse;
import com.teacher.dto.LoginRequest;
import com.teacher.dto.LoginResponse;
import com.teacher.dto.RegisterRequest;
import com.teacher.entity.User;
import com.teacher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest req) {
        User user = userRepo.findByPhoneAndPassword(req.getPhone(), req.getPassword())
                .orElseThrow(() -> new RuntimeException("手机号或密码错误"));
        String token = jwtUtil.generateToken(user.getId(), user.getRole());
        return new LoginResponse(token, user.getId(), user.getName(), user.getRole(), user.getGrade());
    }

    public ApiResponse<?> register(RegisterRequest req) {
        if (userRepo.findByPhone(req.getPhone()).isPresent()) {
            return ApiResponse.error("手机号已注册");
        }
        User user = User.builder()
                .phone(req.getPhone())
                .password(req.getPassword())
                .name(req.getName())
                .grade(req.getGrade())
                .role("student")
                .build();
        userRepo.save(user);
        return ApiResponse.ok("注册成功");
    }

    public User getProfile(Long userId) {
        return userRepo.findById(userId).orElseThrow();
    }
}
