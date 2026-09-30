# AI Agent 系统实现总结

## 概述

基于纯 Java 实现了完整的多 Agent AI 系统，可以打包在 JAR 中独立运行，不依赖外部框架。

## 架构设计

### 四个核心 Agent

1. **EvaluationAgent（评估 Agent）**
   - 职责：分析学生答题情况，生成评估报告
   - 输入：学生信息、测评记录、答题详情
   - 输出：分数、结论、强项、薄弱点、建议、详细分析

2. **DiagnosisAgent（诊断 Agent）**
   - 职责：识别知识薄弱点，分析掌握程度
   - 输入：学生信息、答题详情、各模块统计
   - 输出：知识掌握度映射、薄弱点、强势点、整体诊断、推荐学习路径

3. **PlanningAgent（规划 Agent）**
   - 职责：制定个性化学习计划
   - 输入：学生信息、诊断结果
   - 输出：计划名称、目标、周期、每周计划、每日任务、推荐资料和测试

4. **RecommendationAgent（推荐 Agent）**
   - 职责：推荐学习资料
   - 输入：学生信息、诊断结果、可用资料列表
   - 输出：推荐的资料列表（按优先级排序）

### 工作流程

```
学生答题 → EvaluationAgent（评估表现）
         → DiagnosisAgent（诊断薄弱点）
         → PlanningAgent（制定计划）
         → RecommendationAgent（推荐资料）
```

## 文件结构

```
backend/src/main/java/com/teacher/ai/
├── agent/
│   ├── EvaluationAgent.java      # 评估 Agent
│   ├── DiagnosisAgent.java       # 诊断 Agent
│   ├── PlanningAgent.java        # 规划 Agent
│   └── RecommendationAgent.java  # 推荐 Agent
├── config/
│   ├── AiConfig.java             # AI 配置类
│   └── AiProperties.java         # AI 属性配置
├── controller/
│   └── AiController.java         # AI API 控制器
├── dto/
│   ├── AiAnalysisResult.java     # 完整分析结果
│   ├── DiagnosisResult.java      # 诊断结果
│   ├── EvaluationResult.java     # 评估结果
│   └── PlanningResult.java       # 规划结果
└── service/
    ├── AiAgentService.java       # Agent 编排服务
    └── LlmService.java           # LLM 调用服务
```

## API 接口

### 1. 完整 AI 分析
```
POST /api/ai/analyze/{recordId}
```
返回完整的分析结果（评估 + 诊断 + 规划 + 推荐）

### 2. 快速评估
```
POST /api/ai/evaluate/{recordId}
```
仅返回评估结果

### 3. 快速诊断
```
POST /api/ai/diagnose/{recordId}
```
仅返回诊断结果

## 配置说明

### application.yml
```yaml
app:
  ai:
    enabled: false  # 默认禁用，需要时启用
    endpoint: ${AI_ENDPOINT:}
    api-key: ${AI_API_KEY:}
    model: ${AI_MODEL:}
    temperature: 0.7
    max-tokens: 2000
```

### 本地配置（不提交到 Git）

1. 复制示例文件：
```bash
cp src/main/resources/application-local.yml.example \
   src/main/resources/application-local.yml
```

2. 编辑 `application-local.yml`，填入真实配置：
```yaml
app:
  ai:
    enabled: true
    endpoint: https://api.openai.com/v1
    api-key: your-actual-api-key
    model: gpt-4
```

3. `.gitignore` 已配置排除 `application-local.yml`

## 运行方式

### 1. 启用 AI 功能
设置环境变量或修改 application-local.yml：
```bash
export AI_ENDPOINT=https://api.openai.com/v1
export AI_API_KEY=your-api-key
export AI_MODEL=gpt-4
```

### 2. 启动应用
```bash
java -jar target/teacher-backend-1.0.0.jar
```

### 3. 调用 AI 分析
```bash
curl -X POST http://localhost:8080/api/ai/analyze/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json"
```

## 特点

✅ **纯 Java 实现**：可以打包在 JAR 中，无需外部依赖
✅ **模块化设计**：每个 Agent 独立职责，易于扩展
✅ **LLM 兼容**：支持所有 OpenAI 兼容的 API
✅ **降级处理**：LLM 不可用时返回默认结果
✅ **配置灵活**：支持环境变量和本地配置文件
✅ **安全考虑**：API Key 不提交到 Git

## 与 pi-agent 的对比

| 特性 | pi-agent | 本实现 |
|------|----------|--------|
| 语言 | TypeScript | Java |
| 打包 | 无法打包成 JAR | ✅ 可打包成 JAR |
| 运行 | 需要 Node.js | ✅ 仅需 JRE |
| 集成 | 需要外部进程 | ✅ 进程内调用 |
| 扩展性 | 高 | ✅ 高 |

## 下一步

1. 配置真实的 LLM API Key
2. 测试完整的 AI 分析流程
3. 在前端集成 AI 分析结果展示
4. 根据实际使用效果优化 Prompt

## 注意事项

- AI 功能默认禁用，需要手动启用
- 需要有效的 LLM API Key（如 OpenAI、Claude 等）
- API 调用会产生费用，请注意控制使用量
- 建议在生产环境使用前充分测试
