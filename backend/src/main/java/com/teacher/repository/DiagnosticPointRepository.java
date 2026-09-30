package com.teacher.repository;

import com.teacher.entity.DiagnosticPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticPointRepository extends JpaRepository<DiagnosticPoint, Long> {
    List<DiagnosticPoint> findByModuleId(Long moduleId);
}
