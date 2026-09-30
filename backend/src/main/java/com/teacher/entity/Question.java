package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String questionType = "单选题";

    private String difficulty;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String options;

    private Integer answer;

    @Column(columnDefinition = "TEXT")
    private String analysis;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private KnowledgeModule module;

    @ManyToOne
    @JoinColumn(name = "knowledge_point_id")
    private KnowledgePoint knowledgePoint;
}
