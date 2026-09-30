package com.teacher.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResult {
    private Integer score;
    private String conclusion;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;
    private String detailedAnalysis;
}
