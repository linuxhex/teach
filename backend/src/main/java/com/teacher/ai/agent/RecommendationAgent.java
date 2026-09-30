package com.teacher.ai.agent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teacher.ai.dto.DiagnosisResult;
import com.teacher.ai.service.LlmService;
import com.teacher.entity.Material;
import com.teacher.entity.User;
import com.teacher.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationAgent {

    private final LlmService llmService;
    private final ObjectMapper objectMapper;
    private final MaterialRepository materialRepository;

    public List<Material> recommend(User user, DiagnosisResult diagnosis, int limit) {
        log.info("推荐 Agent 开始推荐学习资料");

        List<Material> allMaterials = materialRepository.findAll();
        
        if (allMaterials.isEmpty()) {
            log.warn("没有可用的学习资料");
            return new ArrayList<>();
        }

        String systemPrompt = buildSystemPrompt();
        String userMessage = buildUserMessage(user, diagnosis, allMaterials);

        Optional<String> response = llmService.chat(systemPrompt, userMessage);

        if (response.isEmpty()) {
            log.warn("推荐 Agent 调用 LLM 失败，使用默认排序");
            return defaultRecommendation(diagnosis, allMaterials, limit);
        }

        try {
            List<Long> recommendedIds = parseResponse(response.get());
            return allMaterials.stream()
                    .filter(m -> recommendedIds.contains(m.getId()))
                    .sorted(Comparator.comparingInt(m -> recommendedIds.indexOf(m.getId())))
                    .limit(limit)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("推荐 Agent 解析响应失败: {}", e.getMessage(), e);
            return defaultRecommendation(diagnosis, allMaterials, limit);
        }
    }

    private String buildSystemPrompt() {
        return """
            你是一个专业的高中数学学习资料推荐专家。你的任务是根据学生的诊断结果，推荐最合适的学习资料。
            
            你需要：
            1. 优先推荐针对薄弱知识点的资料
            2. 考虑资料的难度和学生的年级匹配
            3. 推荐顺序从基础到提高
            4. 推荐数量控制在指定范围内
            
            输出格式要求（JSON）：
            {
              "recommendedIds": [资料ID1, 资料ID2, ...],
              "reason": "推荐理由(50-100字)"
            }
            """;
    }

    private String buildUserMessage(User user, DiagnosisResult diagnosis, List<Material> materials) {
        StringBuilder sb = new StringBuilder();
        sb.append("学生信息：\n");
        sb.append(String.format("- 年级：%s\n", user.getGrade()));
        
        sb.append("\n诊断结果：\n");
        if (!diagnosis.getWeakPoints().isEmpty()) {
            sb.append("- 薄弱知识点：").append(String.join("、", diagnosis.getWeakPoints())).append("\n");
        }

        sb.append("\n可用资料列表：\n");
        for (Material m : materials) {
            sb.append(String.format("ID=%d, 标题=%s, 类型=%s, 作用=%s, 适合=%s\n",
                    m.getId(), m.getTitle(), m.getType(), m.getPurpose(), m.getAudience()));
        }

        sb.append(String.format("\n请推荐最多 %d 个最合适的资料。", materials.size()));
        return sb.toString();
    }

    private List<Long> parseResponse(String response) throws Exception {
        String json = extractJson(response);
        JsonNode node = objectMapper.readTree(json);

        List<Long> ids = new ArrayList<>();
        if (node.has("recommendedIds") && node.get("recommendedIds").isArray()) {
            node.get("recommendedIds").forEach(id -> ids.add(id.asLong()));
        }

        return ids;
    }

    private String extractJson(String response) {
        int start = response.indexOf('{');
        int end = response.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return response.substring(start, end + 1);
        }
        return response;
    }

    private List<Material> defaultRecommendation(DiagnosisResult diagnosis, List<Material> materials, int limit) {
        // 简单排序：优先推荐讲解类，然后刷题类
        return materials.stream()
                .sorted((m1, m2) -> {
                    // 讲解类优先
                    if (m1.getType().equals("讲解类") && !m2.getType().equals("讲解类")) return -1;
                    if (!m1.getType().equals("讲解类") && m2.getType().equals("讲解类")) return 1;
                    return 0;
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}
