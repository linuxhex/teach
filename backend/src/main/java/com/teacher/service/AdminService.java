package com.teacher.service;

import com.teacher.entity.DiagnosticModule;
import com.teacher.entity.DiagnosticPoint;
import com.teacher.entity.KnowledgeModule;
import com.teacher.entity.KnowledgePoint;
import com.teacher.entity.Template;
import com.teacher.repository.DiagnosticModuleRepository;
import com.teacher.repository.DiagnosticPointRepository;
import com.teacher.repository.KnowledgeModuleRepository;
import com.teacher.repository.KnowledgePointRepository;
import com.teacher.repository.TemplateRepository;
import com.teacher.repository.TestRecordRepository;
import com.teacher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    @Autowired
    private KnowledgeModuleRepository moduleRepo;

    @Autowired
    private KnowledgePointRepository pointRepo;

    @Autowired
    private TemplateRepository templateRepo;

    @Autowired
    private DiagnosticModuleRepository diagModuleRepo;

    @Autowired
    private DiagnosticPointRepository diagPointRepo;

    @Autowired
    private TestRecordRepository recordRepo;

    @Autowired
    private UserRepository userRepo;

    public List<KnowledgeModule> getKnowledgeTree() {
        return moduleRepo.findAll();
    }

    public KnowledgeModule addModule(KnowledgeModule m) {
        return moduleRepo.save(m);
    }

    public KnowledgePoint addPoint(KnowledgePoint p) {
        return pointRepo.save(p);
    }

    public void deletePoint(Long id) {
        pointRepo.deleteById(id);
    }

    public List<Template> getTemplates() {
        return templateRepo.findAll();
    }

    public Template addTemplate(Template t) {
        return templateRepo.save(t);
    }

    public void deleteTemplate(Long id) {
        templateRepo.deleteById(id);
    }

    public List<DiagnosticModule> getDiagModules() {
        return diagModuleRepo.findAll();
    }

    public List<DiagnosticPoint> getDiagPoints(Long moduleId) {
        return diagPointRepo.findByModuleId(moduleId);
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("questionCount", 256);
        stats.put("testCount", 1234);
        stats.put("activeUsers", 89);
        stats.put("avgAccuracy", 68);
        stats.put("recentRecords", recordRepo.findAll().stream().limit(5).collect(Collectors.toList()));
        return stats;
    }
}
