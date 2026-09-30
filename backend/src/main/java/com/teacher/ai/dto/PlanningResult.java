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
public class PlanningResult {
    private String planName;
    private String objective;
    private Integer duration; // 天数
    private List<WeeklyPlan> weeklyPlans;
    private List<String> dailyTasks;
    private List<Long> recommendedMaterialIds;
    private List<Long> recommendedTestIds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyPlan {
        private Integer week;
        private String focus;
        private List<String> topics;
        private List<String> tasks;
    }
}
