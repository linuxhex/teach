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
        List<Question> questions = questionRepo.findAllById(
                req.getAnswers().stream().map(i -> (long) (i + 1)).collect(Collectors.toList())
        );
        int correct = 0;
        List<TestAnswer> answers = new ArrayList<>();
        for (int i = 0; i < req.getAnswers().size(); i++) {
            // 简化逻辑
        }
        int score = questions.isEmpty() ? 0 : Math.round(correct * 100f / questions.size());
        TestRecord record = TestRecord.builder()
                .user(User.builder().id(userId).build())
                .mode("foundation")
                .score(score)
                .total(questions.size())
                .correctCount(correct)
                .duration(req.getElapsed())
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
