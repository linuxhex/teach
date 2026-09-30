package com.teacher.ai.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacher.ai.dto.DiagnosisResult;
import com.teacher.ai.service.LlmService;
import com.teacher.entity.Question;
import com.teacher.entity.TestAnswer;
import com.teacher.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiagnosisAgent {

    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public DiagnosisResult diagnose(User user, List<Question> questions, List<TestAnswer> answers) {
        log.info("诊断 Agent 开始分析知识薄弱点");

        String systemPrompt = buildSystemPrompt();
        String userMessage = buildUserMessage(user, questions, answers);

        Optional<String> response = llmService.chat(systemPrompt, userMessage);

        if (response.isEmpty()) {
            log.warn("诊断 Agent 调用 LLM 失败，返回默认结果");
            return buildDefaultResult(questions, answers);
        }

        try {
            return parseResponse(response.get());
        } catch (Exception e) {
            log.error("诊断 Agent 解析响应失败: {}", e.getMessage(), e);
            return buildDefaultResult(questions, answers);
        }
    }

    private String buildSystemPrompt() {
        return """
            你是一个专业的高中数学知识诊断专家。你的任务是分析学生的答题情况，精准识别知识薄弱点。
            
            你需要：
            1. 根据错题分布，识别学生掌握不好的知识点
            2. 分析知识点之间的关联，找出根本问题
            3. 评估每个知识模块的掌握程度（0-100分）
            4. 提供优先级排序的学习路径建议
            
            输出格式要求（JSON）：
            {
              "knowledgeMastery": {"模块名": 掌握度, ...},
              "weakPoints": ["薄弱知识点1", "薄弱知识点2"],
              "strongPoints": ["强势知识点1", "强势知识点2"],
              "overallDiagnosis": "整体诊断(100-150字)",
              "recommendedPaths": [
                {
                  "name": "路径名称",
                  "description": "路径描述",
                  "topics": ["主题1", "主题2"],
                  "estimatedDays": 预计天数
                }
              ],
              "priority": 优先级(1-5, 1最高)
            }
            """;
    }

    private String buildUserMessage(User user, List<Question> questions, List<TestAnswer> answers) {
        StringBuilder sb = new StringBuilder();
        sb.append("学生信息：\n");
        sb.append(String.format("- 年级：%s\n", user.getGrade()));
        
        sb.append("\n答题详情：\n");
        Map<String, List<Boolean>> moduleStats = new HashMap<>();
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            TestAnswer a = answers.get(i);
            
            String module = q.getModule() != null ? q.getModule().getName() : "未知";
            String knowledge = q.getKnowledgePoint() != null ? q.getKnowledgePoint().getName() : "未知";
            
            moduleStats.computeIfAbsent(module, k -> new ArrayList<>()).add(a.getIsCorrect());
            
            sb.append(String.format("%d. [%s] %s - %s (难度：%s)\n", 
                i + 1, 
                a.getIsCorrect() ? "✓" : "✗",
                module,
                knowledge,
                q.getDifficulty()));
        }

        sb.append("\n各模块统计：\n");
        for (Map.Entry<String, List<Boolean>> entry : moduleStats.entrySet()) {
            long correct = entry.getValue().stream().filter(b -> b).count();
            long total = entry.getValue().size();
            sb.append(String.format("- %s: %d/%d (%.0f%%)\n", 
                entry.getKey(), correct, total, correct * 100.0 / total));
        }

        sb.append("\n请根据以上信息，提供详细的知识诊断报告。");
        return sb.toString();
    }

    private DiagnosisResult parseResponse(String response) throws Exception {
        String json = extractJson(response);
        JsonNode node = objectMapper.readTree(json);

        // 解析知识掌握度
        Map<String, Integer> knowledgeMastery = new HashMap<>();
        if (node.has("knowledgeMastery") && node.get("knowledgeMastery").isObject()) {
            node.get("knowledgeMastery").fields().forEachRemaining(entry -> {
                knowledgeMastery.put(entry.getKey(), entry.getValue().asInt());
            });
        }

        // 解析薄弱点
        List<String> weakPoints = new ArrayList<>();
        if (node.has("weakPoints") && node.get("weakPoints").isArray()) {
            node.get("weakPoints").forEach(n -> weakPoints.add(n.asText()));
        }

        // 解析强势点
        List<String> strongPoints = new ArrayList<>();
        if (node.has("strongPoints") && node.get("strongPoints").isArray()) {
            node.get("strongPoints").forEach(n -> strongPoints.add(n.asText()));
        }

        // 解析推荐路径
        List<DiagnosisResult.LearningPath> paths = new ArrayList<>();
        if (node.has("recommendedPaths") && node.get("recommendedPaths").isArray()) {
            for (JsonNode pathNode : node.get("recommendedPaths")) {
                List<String> topics = new ArrayList<>();
                if (pathNode.has("topics") && pathNode.get("topics").isArray()) {
                    pathNode.get("topics").forEach(t -> topics.add(t.asText()));
                }

                paths.add(DiagnosisResult.LearningPath.builder()
                        .name(pathNode.path("name").asText())
                        .description(pathNode.path("description").asText())
                        .topics(topics)
                        .estimatedDays(pathNode.path("estimatedDays").asInt(7))
                        .build());
            }
        }

        return DiagnosisResult.builder()
                .knowledgeMastery(knowledgeMastery)
                .weakPoints(weakPoints)
                .strongPoints(strongPoints)
                .overallDiagnosis(node.path("overallDiagnosis").asText())
                .recommendedPaths(paths)
                .priority(node.path("priority").asInt(3))
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

    private DiagnosisResult buildDefaultResult(List<Question> questions, List<TestAnswer> answers) {
        Map<String, Integer> mastery = new HashMap<>();
        Map<String, List<Boolean>> moduleStats = new HashMap<>();
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            TestAnswer a = answers.get(i);
            String module = q.getModule() != null ? q.getModule().getName() : "未知";
            moduleStats.computeIfAbsent(module, k -> new ArrayList<>()).add(a.getIsCorrect());
        }

        List<String> weakPoints = new ArrayList<>();
        List<String> strongPoints = new ArrayList<>();

        for (Map.Entry<String, List<Boolean>> entry : moduleStats.entrySet()) {
            long correct = entry.getValue().stream().filter(b -> b).count();
            long total = entry.getValue().size();
            int percent = (int) (correct * 100 / total);
            mastery.put(entry.getKey(), percent);

            if (percent < 60) {
                weakPoints.add(entry.getKey());
            } else if (percent >= 80) {
                strongPoints.add(entry.getKey());
            }
        }

        return DiagnosisResult.builder()
                .knowledgeMastery(mastery)
                .weakPoints(weakPoints)
                .strongPoints(strongPoints)
                .overallDiagnosis("AI 诊断暂时不可用，请查看详细答题记录。")
                .recommendedPaths(new ArrayList<>())
                .priority(3)
                .build();
    }
}
