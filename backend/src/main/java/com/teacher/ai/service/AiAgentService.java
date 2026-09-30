package com.teacher.ai.service;

import com.teacher.ai.agent.DiagnosisAgent;
import com.teacher.ai.agent.EvaluationAgent;
import com.teacher.ai.agent.PlanningAgent;
import com.teacher.ai.agent.RecommendationAgent;
import com.teacher.ai.dto.AiAnalysisResult;
import com.teacher.ai.dto.DiagnosisResult;
import com.teacher.ai.dto.EvaluationResult;
import com.teacher.ai.dto.PlanningResult;
import com.teacher.entity.*;
import com.teacher.repository.MaterialRepository;
import com.teacher.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAgentService {

    private final EvaluationAgent evaluationAgent;
    private final DiagnosisAgent diagnosisAgent;
    private final PlanningAgent planningAgent;
    private final RecommendationAgent recommendationAgent;
    private final QuestionRepository questionRepository;
    private final MaterialRepository materialRepository;

    /**
     * 完整的 AI 分析流程：评估 → 诊断 → 规划 → 推荐
     */
    public AiAnalysisResult analyze(User user, TestRecord record) {
        log.info("开始 AI 完整分析流程: userId={}, recordId={}", user.getId(), record.getId());

        // 获取答题详情
        List<TestAnswer> answers = record.getAnswers() != null ? record.getAnswers() : new ArrayList<>();
        List<Long> questionIds = answers.stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toList());
        List<Question> questions = questionIds.isEmpty() ? new ArrayList<>() :
                questionRepository.findAllById(questionIds);

        // 1. 评估 Agent
        EvaluationResult evaluation = evaluationAgent.evaluate(user, record, questions, answers);
        log.info("评估完成: score={}, conclusion={}", evaluation.getScore(), evaluation.getConclusion());

        // 2. 诊断 Agent
        DiagnosisResult diagnosis = diagnosisAgent.diagnose(user, questions, answers);
        log.info("诊断完成: weakPoints={}, strongPoints={}", diagnosis.getWeakPoints().size(), diagnosis.getStrongPoints().size());

        // 3. 规划 Agent
        PlanningResult planning = planningAgent.createPlan(user, diagnosis);
        log.info("规划完成: planName={}, duration={}天", planning.getPlanName(), planning.getDuration());

        // 4. 推荐 Agent
        List<Material> recommendations = recommendationAgent.recommend(user, diagnosis, 5);
        log.info("推荐完成: {} 个资料", recommendations.size());

        return AiAnalysisResult.builder()
                .evaluation(evaluation)
                .diagnosis(diagnosis)
                .planning(planning)
                .recommendations(recommendations)
                .build();
    }

    /**
     * 仅评估（快速模式）
     */
    public EvaluationResult quickEvaluate(User user, TestRecord record) {
        List<TestAnswer> answers = record.getAnswers() != null ? record.getAnswers() : new ArrayList<>();
        List<Long> questionIds = answers.stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toList());
        List<Question> questions = questionIds.isEmpty() ? new ArrayList<>() :
                questionRepository.findAllById(questionIds);

        return evaluationAgent.evaluate(user, record, questions, answers);
    }

    /**
     * 仅诊断
     */
    public DiagnosisResult quickDiagnose(User user, TestRecord record) {
        List<TestAnswer> answers = record.getAnswers() != null ? record.getAnswers() : new ArrayList<>();
        List<Long> questionIds = answers.stream()
                .map(a -> a.getQuestion().getId())
                .collect(Collectors.toList());
        List<Question> questions = questionIds.isEmpty() ? new ArrayList<>() :
                questionRepository.findAllById(questionIds);

        return diagnosisAgent.diagnose(user, questions, answers);
    }
}
