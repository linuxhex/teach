package com.teacher.repository;

import com.teacher.entity.KnowledgePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnowledgePointRepository extends JpaRepository<KnowledgePoint, Long> {
    List<KnowledgePoint> findByModuleId(Long moduleId);
}
