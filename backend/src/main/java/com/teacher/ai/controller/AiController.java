package com.teacher.ai.controller;

import com.teacher.ai.dto.AiAnalysisResult;
import com.teacher.ai.dto.DiagnosisResult;
import com.teacher.ai.dto.EvaluationResult;
import com.teacher.ai.service.AiAgentService;
import com.teacher.dto.ApiResponse;
import com.teacher.entity.TestRecord;
import com.teacher.entity.User;
import com.teacher.repository.TestRecordRepository;
import com.teacher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiAgentService aiAgentService;
    private final TestRecordRepository testRecordRepository;
    private final UserRepository userRepository;

    /**
     * 完整 AI 分析（评估 + 诊断 + 规划 + 推荐）
     */
    @PostMapping("/analyze/{recordId}")
    public ApiResponse<AiAnalysisResult> analyze(@PathVariable Long recordId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        TestRecord record = testRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("测评记录不存在"));

        if (!record.getUser().getId().equals(userId)) {
            return ApiResponse.error("无权访问此记录");
        }

        AiAnalysisResult result = aiAgentService.analyze(user, record);
        return ApiResponse.ok(result);
    }

    /**
     * 快速评估
     */
    @PostMapping("/evaluate/{recordId}")
    public ApiResponse<EvaluationResult> evaluate(@PathVariable Long recordId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        TestRecord record = testRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("测评记录不存在"));

        if (!record.getUser().getId().equals(userId)) {
            return ApiResponse.error("无权访问此记录");
        }

        EvaluationResult result = aiAgentService.quickEvaluate(user, record);
        return ApiResponse.ok(result);
    }

    /**
     * 快速诊断
     */
    @PostMapping("/diagnose/{recordId}")
    public ApiResponse<DiagnosisResult> diagnose(@PathVariable Long recordId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        TestRecord record = testRecordRepository.findById(recordId).orElseThrow(() -> new RuntimeException("测评记录不存在"));

        if (!record.getUser().getId().equals(userId)) {
            return ApiResponse.error("无权访问此记录");
        }

        DiagnosisResult result = aiAgentService.quickDiagnose(user, record);
        return ApiResponse.ok(result);
    }
}
