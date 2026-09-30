package com.teacher.repository;

import com.teacher.entity.KnowledgeModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeModuleRepository extends JpaRepository<KnowledgeModule, Long> {
}
