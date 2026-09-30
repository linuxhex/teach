package com.teacher.controller;

import com.teacher.dto.ApiResponse;
import com.teacher.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @GetMapping
    public ApiResponse<?> list(@RequestParam(required = false) String type,
                               @RequestParam(required = false) String purpose,
                               @RequestParam(required = false) String keyword) {
        return ApiResponse.ok(materialService.list(type, purpose, keyword));
    }

    @GetMapping("/filters")
    public ApiResponse<?> filters() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("stages", List.of("同步学习", "高三复习", "暑假", "寒假"));
        filters.put("versions", List.of("人教A版", "人教B版", "北师大版"));
        filters.put("types", List.of("讲解类", "刷题类", "功能类"));
        filters.put("purposes", List.of("章节体系", "专题突破", "工具资料"));
        return ApiResponse.ok(filters);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) {
        return ApiResponse.ok(materialService.getById(id));
    }
}
