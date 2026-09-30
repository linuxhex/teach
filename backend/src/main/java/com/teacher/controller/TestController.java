package com.teacher.controller;

import com.teacher.dto.ApiResponse;
import com.teacher.dto.SubmitAnswerRequest;
import com.teacher.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/questions")
    public ApiResponse<?> questions(@RequestParam String mode, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.ok(testService.getQuestions(mode, userId));
    }

    @PostMapping("/submit")
    public ApiResponse<?> submit(@RequestBody SubmitAnswerRequest req, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            return ApiResponse.ok(testService.submit(userId, req));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/records")
    public ApiResponse<?> records(@RequestParam(required = false) String mode, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ApiResponse.ok(testService.getRecords(userId, mode));
    }
}
