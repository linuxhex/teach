package com.teacher.service;

import com.teacher.ai.service.SubjectiveGradingService;
import com.teacher.dto.SubmitAnswerRequest;
import com.teacher.dto.SubmitAnswerResponse;
import com.teacher.entity.Question;
import com.teacher.entity.TestAnswer;
import com.teacher.entity.TestRecord;
import com.teacher.entity.User;
import com.teacher.repository.QuestionRepository;
import com.teacher.repository.TestRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private TestRecordRepository recordRepo;

    @Autowired
    private SubjectiveGradingService subjectiveGradingService;

    public List<Question> getQuestions(String mode, Long userId) {
        List<Question> all = questionRepo.findAll();
        Collections.shuffle(all);
        // 确保包含主观题和客观题
        List<Question> objective = all.stream()
                .filter(q -> "单选题".equals(q.getQuestionType()))
                .collect(Collectors.toList());
        List<Question> subjective = all.stream()
                .filter(q -> "主观题".equals(q.getQuestionType()))
                .collect(Collectors.toList());
        
        Collections.shuffle(objective);
        Collections.shuffle(subjective);
        
        List<Question> result = new ArrayList<>();
        result.addAll(objective.subList(0, Math.min(6, objective.size())));
        result.addAll(subjective.subList(0, Math.min(2, subjective.size())));
        Collections.shuffle(result);
        return result;
    }

    public SubmitAnswerResponse submit(Long userId, SubmitAnswerRequest req) {
        List<Long> questionIds = req.getAnswers().stream()
                .map(SubmitAnswerRequest.AnswerItem::getQuestionId)
                .collect(Collectors.toList());
        
        List<Question> questions = questionRepo.findAllById(questionIds);
        
        int correct = 0;
        int totalSubjectiveScore = 0;
        int totalSubjectiveMaxScore = 0;
        List<TestAnswer> answers = new ArrayList<>();
        List<TestAnswer> subjectiveAnswers = new ArrayList<>();
        
        for (SubmitAnswerRequest.AnswerItem answerItem : req.getAnswers()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answerItem.getQuestionId()))
                    .findFirst()
                    .orElse(null);
            
            if (question != null) {
                TestAnswer testAnswer = TestAnswer.builder()
                        .question(question)
                        .userAnswer(answerItem.getAnswer())
                        .textAnswer(answerItem.getTextAnswer())
                        .build();
                
                if ("主观题".equals(question.getQuestionType())) {
                    // 主观题：稍后由AI评分
                    testAnswer.setIsCorrect(null);
                    subjectiveAnswers.add(testAnswer);
                    totalSubjectiveMaxScore += 10; // 每题10分
                } else {
                    // 客观题：直接判断对错
                    boolean isCorrect = answerItem.getAnswer() != null && 
                                       answerItem.getAnswer().equals(question.getAnswer());
                    testAnswer.setIsCorrect(isCorrect);
                    if (isCorrect) {
                        correct++;
                    }
                }
                answers.add(testAnswer);
            }
        }
        
        // AI评分主观题
        if (!subjectiveAnswers.isEmpty()) {
            for (TestAnswer answer : subjectiveAnswers) {
                if (answer.getTextAnswer() != null && !answer.getTextAnswer().trim().isEmpty()) {
                    SubjectiveGradingService.GradingResult result = 
                        subjectiveGradingService.grade(answer.getQuestion(), answer.getTextAnswer());
                    answer.setAiScore(result.getScore());
                    answer.setAiFeedback(result.getFeedback());
                    answer.setIsCorrect(result.getScore() >= 6); // 6分以上算正确
                    if (result.getScore() >= 6) {
                        correct++;
                    }
                    totalSubjectiveScore += result.getScore();
                } else {
                    answer.setAiScore(0);
                    answer.setAiFeedback("未作答");
                    answer.setIsCorrect(false);
                }
            }
        }
        
        // 计算总分：客观题占60%，主观题占40%
        int objectiveCount = answers.size() - subjectiveAnswers.size();
        int objectiveScore = objectiveCount > 0 ? Math.round(correct * 60f / objectiveCount) : 0;
        int subjectiveScorePercent = totalSubjectiveMaxScore > 0 ? 
            Math.round(totalSubjectiveScore * 40f / totalSubjectiveMaxScore) : 0;
        int score = objectiveScore + subjectiveScorePercent;
        
        TestRecord record = TestRecord.builder()
                .user(User.builder().id(userId).build())
                .mode("foundation")
                .score(score)
                .total(questions.size())
                .correctCount(correct)
                .duration(req.getElapsed())
                .answers(answers)
                .build();
        
        record = recordRepo.save(record);
        return new SubmitAnswerResponse(score, questions.size(), correct, record.getId());
    }

    public List<TestRecord> getRecords(Long userId, String mode) {
        if (mode != null) {
            return recordRepo.findByUserIdAndModeOrderByCreatedAtDesc(userId, mode);
        }
        return recordRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
