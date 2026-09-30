package com.teacher.ai.dto;

import com.teacher.entity.Material;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiAnalysisResult {
    private EvaluationResult evaluation;
    private DiagnosisResult diagnosis;
    private PlanningResult planning;
    private List<Material> recommendations;
}
