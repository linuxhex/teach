package com.teacher.controller;

import com.teacher.dto.ApiResponse;
import com.teacher.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/progress")
    public ApiResponse<?> progress(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.ok(userService.getProgress(userId));
    }

    @GetMapping("/knowledge-map")
    public ApiResponse<?> knowledgeMap(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.ok(userService.getKnowledgeMap(userId));
    }
}
