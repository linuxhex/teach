package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "knowledge_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    private Integer questionCount = 0;

    private String difficulty;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    private Integer sortOrder = 0;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private KnowledgeModule module;
}
