package com.teacher.controller;

import com.teacher.dto.ApiResponse;
import com.teacher.entity.KnowledgeModule;
import com.teacher.entity.KnowledgePoint;
import com.teacher.entity.Template;
import com.teacher.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/knowledge/tree")
    public ApiResponse<?> knowledgeTree() {
        return ApiResponse.ok(adminService.getKnowledgeTree());
    }

    @PostMapping("/knowledge/module")
    public ApiResponse<?> addModule(@RequestBody KnowledgeModule m) {
        return ApiResponse.ok(adminService.addModule(m));
    }

    @PostMapping("/knowledge/point")
    public ApiResponse<?> addPoint(@RequestBody KnowledgePoint p) {
        return ApiResponse.ok(adminService.addPoint(p));
    }

    @DeleteMapping("/knowledge/point/{id}")
    public ApiResponse<?> deletePoint(@PathVariable Long id) {
        adminService.deletePoint(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/templates")
    public ApiResponse<?> templates() {
        return ApiResponse.ok(adminService.getTemplates());
    }

    @PostMapping("/templates")
    public ApiResponse<?> addTemplate(@RequestBody Template t) {
        return ApiResponse.ok(adminService.addTemplate(t));
    }

    @DeleteMapping("/templates/{id}")
    public ApiResponse<?> deleteTemplate(@PathVariable Long id) {
        adminService.deleteTemplate(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/diagnostic-modules")
    public ApiResponse<?> diagModules() {
        return ApiResponse.ok(adminService.getDiagModules());
    }

    @GetMapping("/diagnostic-points")
    public ApiResponse<?> diagPoints(@RequestParam(required = false) Long moduleId) {
        return ApiResponse.ok(adminService.getDiagPoints(moduleId));
    }

    @GetMapping("/stats")
    public ApiResponse<?> stats() {
        return ApiResponse.ok(adminService.getStats());
    }
}
