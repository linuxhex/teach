package com.teacher.ai.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "app.ai")
@Data
public class AiProperties {
    
    private static final Logger log = LoggerFactory.getLogger(AiProperties.class);
    private static final String EXTERNAL_CONFIG_PATH = System.getProperty("user.home") + "/.teacher/ai-config.properties";
    
    private boolean enabled = false;
    private String endpoint;
    private String apiKey;
    private String model;
    private Double temperature = 0.7;
    private Integer maxTokens = 2000;
    private String systemPrompt;
    
    @PostConstruct
    public void loadExternalConfig() {
        File externalFile = new File(EXTERNAL_CONFIG_PATH);
        if (externalFile.exists()) {
            log.info("加载外部 AI 配置文件: {}", EXTERNAL_CONFIG_PATH);
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(externalFile)) {
                props.load(fis);
                
                // 覆盖配置
                if (props.containsKey("app.ai.enabled")) {
                    this.enabled = Boolean.parseBoolean(props.getProperty("app.ai.enabled"));
                }
                if (props.containsKey("app.ai.endpoint")) {
                    this.endpoint = props.getProperty("app.ai.endpoint");
                }
                if (props.containsKey("app.ai.api-key")) {
                    this.apiKey = props.getProperty("app.ai.api-key");
                }
                if (props.containsKey("app.ai.model")) {
                    this.model = props.getProperty("app.ai.model");
                }
                if (props.containsKey("app.ai.temperature")) {
                    this.temperature = Double.parseDouble(props.getProperty("app.ai.temperature"));
                }
                if (props.containsKey("app.ai.max-tokens")) {
                    this.maxTokens = Integer.parseInt(props.getProperty("app.ai.max-tokens"));
                }
                if (props.containsKey("app.ai.system-prompt")) {
                    this.systemPrompt = props.getProperty("app.ai.system-prompt");
                }
                
                log.info("外部 AI 配置加载成功，enabled={}, model={}", this.enabled, this.model);
            } catch (IOException e) {
                log.error("加载外部 AI 配置文件失败: {}", e.getMessage(), e);
            }
        } else {
            log.warn("外部 AI 配置文件不存在: {}，将使用默认配置", EXTERNAL_CONFIG_PATH);
        }
    }
}
