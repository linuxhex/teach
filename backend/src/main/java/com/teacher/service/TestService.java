package com.teacher.service;

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

    public List<Question> getQuestions(String mode, Long userId) {
        List<Question> all = questionRepo.findAll();
        Collections.shuffle(all);
        return all.subList(0, Math.min(8, all.size()));
    }

    public SubmitAnswerResponse submit(Long userId, SubmitAnswerRequest req) {
        List<Long> questionIds = req.getAnswers().stream()
                .map(SubmitAnswerRequest.AnswerItem::getQuestionId)
                .collect(Collectors.toList());
        
        List<Question> questions = questionRepo.findAllById(questionIds);
        
        int correct = 0;
        List<TestAnswer> answers = new ArrayList<>();
        
        for (SubmitAnswerRequest.AnswerItem answerItem : req.getAnswers()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answerItem.getQuestionId()))
                    .findFirst()
                    .orElse(null);
            
            if (question != null) {
                boolean isCorrect = answerItem.getAnswer() != null && 
                                   answerItem.getAnswer().equals(question.getAnswer());
                if (isCorrect) {
                    correct++;
                }
                
                TestAnswer testAnswer = TestAnswer.builder()
                        .question(question)
                        .userAnswer(answerItem.getAnswer())
                        .isCorrect(isCorrect)
                        .build();
                answers.add(testAnswer);
            }
        }
        
        int score = questions.isEmpty() ? 0 : Math.round(correct * 100f / questions.size());
        
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
