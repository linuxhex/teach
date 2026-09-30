package com.teacher.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisResult {
    private Map<String, Integer> knowledgeMastery; // 知识点掌握度 0-100
    private List<String> weakPoints; // 薄弱知识点
    private List<String> strongPoints; // 强势知识点
    private String overallDiagnosis; // 整体诊断
    private List<LearningPath> recommendedPaths; // 推荐学习路径
    private Integer priority; // 优先级 1-5

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LearningPath {
        private String name;
        private String description;
        private List<String> topics;
        private Integer estimatedDays;
    }
}
