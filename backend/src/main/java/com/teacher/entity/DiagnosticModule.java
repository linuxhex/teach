package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "diagnostic_modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder.Default
    private Integer diagnosticPointsCount = 0;

    @Builder.Default
    private Integer questionPositions = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DiagnosticPoint> points;
}
