package com.teacher.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.teacher.ai.config.AiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Service
public class LlmService {

    private static final Logger log = LoggerFactory.getLogger(LlmService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AiProperties aiProperties;

    public LlmService(RestTemplate restTemplate, ObjectMapper objectMapper, AiProperties aiProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.aiProperties = aiProperties;
    }

    public Optional<String> chat(String systemPrompt, String userMessage) {
        if (!aiProperties.isEnabled()) {
            log.warn("AI 未启用");
            return Optional.empty();
        }

        String apiKey = aiProperties.getApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.warn("AI API Key 未配置");
            return Optional.empty();
        }

        String endpoint = aiProperties.getEndpoint();
        String model = aiProperties.getModel();
        if (endpoint == null || model == null) {
            log.warn("AI endpoint 或 model 未配置");
            return Optional.empty();
        }

        String url = buildChatCompletionsUrl(endpoint);
        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", model);
        body.put("temperature", aiProperties.getTemperature());
        
        if (aiProperties.getMaxTokens() != null && aiProperties.getMaxTokens() > 0) {
            body.put("max_tokens", aiProperties.getMaxTokens());
        }

        ArrayNode messages = body.putArray("messages");
        
        // System message
        ObjectNode sys = messages.addObject();
        sys.put("role", "system");
        sys.put("content", systemPrompt != null ? systemPrompt : "你是专业的教育助手。");

        // User message
        ObjectNode user = messages.addObject();
        user.put("role", "user");
        user.put("content", userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey.trim());

        try {
            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
            ResponseEntity<String> resp = restTemplate.postForEntity(url, entity, String.class);
            
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("LLM HTTP 非成功: status={}", resp.getStatusCode());
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(resp.getBody());
            if (root.has("error")) {
                log.warn("LLM 返回 error: {}", root.path("error").path("message").asText());
                return Optional.empty();
            }

            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.size() == 0) {
                return Optional.empty();
            }

            String content = choices.get(0).path("message").path("content").asText(null);
            if (content == null || content.trim().isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(content.trim());
        } catch (Exception e) {
            log.error("LLM 调用失败: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<String> chat(String userMessage) {
        return chat(aiProperties.getSystemPrompt(), userMessage);
    }

    private String buildChatCompletionsUrl(String endpoint) {
        String base = endpoint.endsWith("/") ? endpoint.substring(0, endpoint.length() - 1) : endpoint;
        if (base.endsWith("/chat/completions")) {
            return base;
        }
        return base + "/chat/completions";
    }
}
