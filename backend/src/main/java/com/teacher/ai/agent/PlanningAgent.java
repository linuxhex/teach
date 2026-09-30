package com.teacher.ai.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacher.ai.dto.DiagnosisResult;
import com.teacher.ai.dto.PlanningResult;
import com.teacher.ai.service.LlmService;
import com.teacher.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanningAgent {

    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public PlanningResult createPlan(User user, DiagnosisResult diagnosis) {
        log.info("规划 Agent 开始制定学习计划");

        String systemPrompt = buildSystemPrompt();
        String userMessage = buildUserMessage(user, diagnosis);

        Optional<String> response = llmService.chat(systemPrompt, userMessage);

        if (response.isEmpty()) {
            log.warn("规划 Agent 调用 LLM 失败，返回默认结果");
            return buildDefaultResult(diagnosis);
        }

        try {
            return parseResponse(response.get());
        } catch (Exception e) {
            log.error("规划 Agent 解析响应失败: {}", e.getMessage(), e);
            return buildDefaultResult(diagnosis);
        }
    }

    private String buildSystemPrompt() {
        return """
            你是一个专业的高中数学学习规划专家。你的任务是根据学生的诊断结果，制定个性化的学习计划。
            
            你需要：
            1. 根据薄弱知识点，制定针对性的学习路径
            2. 合理安排学习顺序，由易到难
            3. 设定每周学习目标和任务
            4. 预估学习周期（天数）
            
            输出格式要求（JSON）：
            {
              "planName": "计划名称",
              "objective": "学习目标",
              "duration": 总天数,
              "weeklyPlans": [
                {
                  "week": 第几周,
                  "focus": "本周重点",
                  "topics": ["主题1", "主题2"],
                  "tasks": ["任务1", "任务2"]
                }
              ],
              "dailyTasks": ["每日任务1", "每日任务2"],
              "recommendedMaterialIds": [资料ID1, 资料ID2],
              "recommendedTestIds": [测试ID1, 测试ID2]
            }
            """;
    }

    private String buildUserMessage(User user, DiagnosisResult diagnosis) {
        StringBuilder sb = new StringBuilder();
        sb.append("学生信息：\n");
        sb.append(String.format("- 年级：%s\n", user.getGrade()));
        
        sb.append("\n诊断结果：\n");
        sb.append(String.format("- 整体诊断：%s\n", diagnosis.getOverallDiagnosis()));
        
        if (!diagnosis.getWeakPoints().isEmpty()) {
            sb.append("- 薄弱知识点：").append(String.join("、", diagnosis.getWeakPoints())).append("\n");
        }
        
        if (!diagnosis.getStrongPoints().isEmpty()) {
            sb.append("- 强势知识点：").append(String.join("、", diagnosis.getStrongPoints())).append("\n");
        }

        sb.append("\n知识掌握度：\n");
        diagnosis.getKnowledgeMastery().forEach((module, percent) -> {
            sb.append(String.format("- %s: %d%%\n", module, percent));
        });

        sb.append("\n请根据以上信息，制定一个个性化的学习计划。");
        return sb.toString();
    }

    private PlanningResult parseResponse(String response) throws Exception {
        String json = extractJson(response);
        JsonNode node = objectMapper.readTree(json);

        // 解析每周计划
        List<PlanningResult.WeeklyPlan> weeklyPlans = new ArrayList<>();
        if (node.has("weeklyPlans") && node.get("weeklyPlans").isArray()) {
            for (JsonNode weekNode : node.get("weeklyPlans")) {
                List<String> topics = new ArrayList<>();
                if (weekNode.has("topics") && weekNode.get("topics").isArray()) {
                    weekNode.get("topics").forEach(t -> topics.add(t.asText()));
                }

                List<String> tasks = new ArrayList<>();
                if (weekNode.has("tasks") && weekNode.get("tasks").isArray()) {
                    weekNode.get("tasks").forEach(t -> tasks.add(t.asText()));
                }

                weeklyPlans.add(PlanningResult.WeeklyPlan.builder()
                        .week(weekNode.path("week").asInt())
                        .focus(weekNode.path("focus").asText())
                        .topics(topics)
                        .tasks(tasks)
                        .build());
            }
        }

        // 解析每日任务
        List<String> dailyTasks = new ArrayList<>();
        if (node.has("dailyTasks") && node.get("dailyTasks").isArray()) {
            node.get("dailyTasks").forEach(t -> dailyTasks.add(t.asText()));
        }

        // 解析推荐资料ID
        List<Long> materialIds = new ArrayList<>();
        if (node.has("recommendedMaterialIds") && node.get("recommendedMaterialIds").isArray()) {
            node.get("recommendedMaterialIds").forEach(id -> materialIds.add(id.asLong()));
        }

        // 解析推荐测试ID
        List<Long> testIds = new ArrayList<>();
        if (node.has("recommendedTestIds") && node.get("recommendedTestIds").isArray()) {
            node.get("recommendedTestIds").forEach(id -> testIds.add(id.asLong()));
        }

        return PlanningResult.builder()
                .planName(node.path("planName").asText())
                .objective(node.path("objective").asText())
                .duration(node.path("duration").asInt(30))
                .weeklyPlans(weeklyPlans)
                .dailyTasks(dailyTasks)
                .recommendedMaterialIds(materialIds)
                .recommendedTestIds(testIds)
                .build();
    }

    private String extractJson(String response) {
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }

    private PlanningResult buildDefaultResult(DiagnosisResult diagnosis) {
        List<PlanningResult.WeeklyPlan> weeklyPlans = new ArrayList<>();
        
        if (!diagnosis.getWeakPoints().isEmpty()) {
            weeklyPlans.add(PlanningResult.WeeklyPlan.builder()
                    .week(1)
                    .focus("薄弱知识点强化")
                    .topics(new ArrayList<>(diagnosis.getWeakPoints()))
                    .tasks(List.of("复习基础知识", "做专项练习", "整理错题"))
                    .build());
        }

        return PlanningResult.builder()
                .planName("个性化提升计划")
                .objective("针对薄弱知识点进行系统提升")
                .duration(30)
                .weeklyPlans(weeklyPlans)
                .dailyTasks(List.of("每日练习30分钟", "整理错题本", "复习知识点"))
                .recommendedMaterialIds(new ArrayList<>())
                .recommendedTestIds(new ArrayList<>())
                .build();
    }
}
