package com.teacher.ai.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacher.ai.dto.EvaluationResult;
import com.teacher.ai.service.LlmService;
import com.teacher.entity.Question;
import com.teacher.entity.TestAnswer;
import com.teacher.entity.TestRecord;
import com.teacher.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvaluationAgent {

    private final LlmService llmService;
    private final ObjectMapper objectMapper;

    public EvaluationResult evaluate(User user, TestRecord record, List<Question> questions, List<TestAnswer> answers) {
        log.info("评估 Agent 开始分析学生答题情况");

        String systemPrompt = buildSystemPrompt();
        String userMessage = buildUserMessage(user, record, questions, answers);

        Optional<String> response = llmService.chat(systemPrompt, userMessage);

        if (response.isEmpty()) {
            log.warn("评估 Agent 调用 LLM 失败，返回默认结果");
            return buildDefaultResult(record);
        }

        try {
            return parseResponse(response.get());
        } catch (Exception e) {
            log.error("评估 Agent 解析响应失败: {}", e.getMessage(), e);
            return buildDefaultResult(record);
        }
    }

    private String buildSystemPrompt() {
        return """
            你是一个专业的高中数学教育评估专家。你的任务是分析学生的答题情况，提供客观、专业的评估报告。
            
            你需要：
            1. 根据学生的答题正确率和错题分布，评估整体表现
            2. 识别学生的知识强项和薄弱环节
            3. 提供具体、可操作的学习建议
            4. 使用鼓励性的语言，帮助学生建立信心
            
            输出格式要求（JSON）：
            {
              "score": 分数(0-100),
              "conclusion": "总体结论(一句话)",
              "strengths": ["强项1", "强项2"],
              "weaknesses": ["薄弱点1", "薄弱点2"],
              "suggestions": ["建议1", "建议2", "建议3"],
              "detailedAnalysis": "详细分析(200-300字)"
            }
            """;
    }

    private String buildUserMessage(User user, TestRecord record, List<Question> questions, List<TestAnswer> answers) {
        StringBuilder sb = new StringBuilder();
        sb.append("学生信息：\n");
        sb.append(String.format("- 姓名：%s\n", user.getName()));
        sb.append(String.format("- 年级：%s\n", user.getGrade()));
        sb.append(String.format("- 测评得分：%d/%d\n", record.getScore(), record.getTotal()));
        sb.append(String.format("- 正确题数：%d\n", record.getCorrectCount()));
        sb.append(String.format("- 用时：%d 秒\n", record.getDuration()));
        
        sb.append("\n答题详情：\n");
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            TestAnswer a = answers.get(i);
            sb.append(String.format("%d. [%s] %s - %s\n", 
                i + 1, 
                a.getIsCorrect() ? "✓" : "✗",
                q.getModule() != null ? q.getModule().getName() : "未知模块",
                q.getKnowledgePoint() != null ? q.getKnowledgePoint().getName() : "未知知识点"));
        }

        sb.append("\n请根据以上信息，提供详细的评估报告。");
        return sb.toString();
    }

    private EvaluationResult parseResponse(String response) throws Exception {
        // 尝试从响应中提取 JSON
        String json = extractJson(response);
        JsonNode node = objectMapper.readTree(json);

        List<String> strengths = new ArrayList<>();
        if (node.has("strengths") && node.get("strengths").isArray()) {
            node.get("strengths").forEach(n -> strengths.add(n.asText()));
        }

        List<String> weaknesses = new ArrayList<>();
        if (node.has("weaknesses") && node.get("weaknesses").isArray()) {
            node.get("weaknesses").forEach(n -> weaknesses.add(n.asText()));
        }

        List<String> suggestions = new ArrayList<>();
        if (node.has("suggestions") && node.get("suggestions").isArray()) {
            node.get("suggestions").forEach(n -> suggestions.add(n.asText()));
        }

        return EvaluationResult.builder()
                .score(node.path("score").asInt(0))
                .conclusion(node.path("conclusion").asText(""))
                .strengths(strengths)
                .weaknesses(weaknesses)
                .suggestions(suggestions)
                .detailedAnalysis(node.path("detailedAnalysis").asText(""))
                .build();
    }

    private String extractJson(String response) {
        // 尝试提取 JSON 块
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }

    private EvaluationResult buildDefaultResult(TestRecord record) {
        int score = record.getScore();
        String conclusion;
        if (score >= 80) {
            conclusion = "表现优秀，继续保持！";
        } else if (score >= 60) {
            conclusion = "基础尚可，还有提升空间。";
        } else {
            conclusion = "需要加强基础，建议系统复习。";
        }

        List<String> suggestions = new ArrayList<>();
        suggestions.add("针对错题进行专项练习");
        suggestions.add("复习相关知识点");
        suggestions.add("多做类似题型巩固");

        return EvaluationResult.builder()
                .score(score)
                .conclusion(conclusion)
                .strengths(new ArrayList<>())
                .weaknesses(new ArrayList<>())
                .suggestions(suggestions)
                .detailedAnalysis("AI 评估暂时不可用，请查看详细答题记录。")
                .build();
    }
}
