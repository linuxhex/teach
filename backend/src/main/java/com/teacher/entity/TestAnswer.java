package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "test_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userAnswer;

    @Column(columnDefinition = "TEXT")
    private String textAnswer;

    private Boolean isCorrect;

    private Integer aiScore;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private TestRecord record;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
