package com.teacher.ai.service;

import com.teacher.ai.config.AiProperties;
import com.teacher.entity.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectiveGradingService {

    private final LlmService llmService;
    private final AiProperties aiProperties;

    public GradingResult grade(Question question, String studentAnswer) {
        log.info("开始AI评分主观题: questionId={}, 答案长度={}", question.getId(), 
                studentAnswer != null ? studentAnswer.length() : 0);

        if (!aiProperties.isEnabled()) {
            log.warn("AI 未启用，使用默认评分");
            return buildDefaultResult();
        }

        String systemPrompt = buildSystemPrompt();
        String userMessage = buildUserMessage(question, studentAnswer);

        Optional<String> response = llmService.chat(systemPrompt, userMessage);

        if (response.isEmpty()) {
            log.warn("AI 评分失败，使用默认评分");
            return buildDefaultResult();
        }

        try {
            return parseResponse(response.get());
        } catch (Exception e) {
            log.error("AI 评分解析失败: {}", e.getMessage(), e);
            return buildDefaultResult();
        }
    }

    private String buildSystemPrompt() {
        return """
            你是一个专业的高中数学教师，正在批改学生的主观题解答。
            
            你需要：
            1. 仔细分析学生的解答过程
            2. 判断解题思路是否正确
            3. 检查计算过程是否准确
            4. 评估答案的完整性
            5. 给出0-10分的评分（10分为满分）
            6. 提供具体的反馈意见
            
            评分标准：
            - 10分：思路正确，计算准确，答案完整
            - 8-9分：思路正确，有小计算错误
            - 6-7分：思路基本正确，有部分错误
            - 4-5分：思路有一定问题，但有可取之处
            - 2-3分：思路有明显错误
            - 0-1分：完全错误或未作答
            
            输出格式要求（JSON）：
            {
              "score": 分数(0-10的整数),
              "feedback": "反馈意见(50-100字，指出优点和不足)"
            }
            """;
    }

    private String buildUserMessage(Question question, String studentAnswer) {
        String referenceAnswer = question.getReferenceAnswer() != null ? 
            question.getReferenceAnswer() : "无参考答案";
        return String.format("""
            题目：%s
            
            参考答案：%s
            
            学生解答：%s
            
            请评分并给出反馈意见。
            """, question.getContent(), referenceAnswer, studentAnswer);
    }

    private GradingResult parseResponse(String response) {
        try {
            // 尝试从响应中提取JSON
            String json = extractJson(response);
            if (json != null) {
                // 简单解析JSON
                int scoreStart = json.indexOf("\"score\"");
                if (scoreStart != -1) {
                    int colonPos = json.indexOf(":", scoreStart);
                    int commaPos = json.indexOf(",", colonPos);
                    if (commaPos == -1) commaPos = json.indexOf("}", colonPos);
                    String scoreStr = json.substring(colonPos + 1, commaPos).trim();
                    int score = Integer.parseInt(scoreStr);
                    
                    int feedbackStart = json.indexOf("\"feedback\"");
                    if (feedbackStart != -1) {
                        int quoteStart = json.indexOf("\"", feedbackStart + 10);
                        int quoteEnd = json.indexOf("\"", quoteStart + 1);
                        String feedback = json.substring(quoteStart + 1, quoteEnd);
                        
                        return new GradingResult(score, feedback);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析AI评分响应失败: {}", e.getMessage());
        }
        return buildDefaultResult();
    }

    private String extractJson(String response) {
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");
        if (start != -1 && end != -1 && end > start) {
            return response.substring(start, end + 1);
        }
        return null;
    }

    private GradingResult buildDefaultResult() {
        return new GradingResult(5, "AI评分暂时不可用，建议复习相关知识点。");
    }

    public static class GradingResult {
        private final int score;
        private final String feedback;

        public GradingResult(int score, String feedback) {
            this.score = score;
            this.feedback = feedback;
        }

        public int getScore() {
            return score;
        }

        public String getFeedback() {
            return feedback;
        }
    }
}
