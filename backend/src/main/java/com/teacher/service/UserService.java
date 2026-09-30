package com.teacher.service;

import com.teacher.entity.KnowledgeModule;
import com.teacher.entity.TestRecord;
import com.teacher.repository.KnowledgeModuleRepository;
import com.teacher.repository.TestRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private KnowledgeModuleRepository moduleRepo;

    @Autowired
    private TestRecordRepository recordRepo;

    public Map<String, Object> getProgress(Long userId) {
        List<TestRecord> records = recordRepo.findByUserIdOrderByCreatedAtDesc(userId);
        int testCount = records.size();
        double avgScore = records.stream().mapToInt(TestRecord::getScore).average().orElse(0);
        Map<String, Object> result = new HashMap<>();
        result.put("testCount", testCount);
        result.put("accuracyRate", Math.round(avgScore));
        result.put("masteredTypes", 12);
        result.put("studyDays", 30);
        return result;
    }

    public List<KnowledgeModule> getKnowledgeMap(Long userId) {
        return moduleRepo.findAll();
    }
}
