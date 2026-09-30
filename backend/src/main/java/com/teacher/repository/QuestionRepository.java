package com.teacher.repository;

import com.teacher.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByModuleId(Long moduleId);
    List<Question> findByKnowledgePointId(Long kpId);
}
