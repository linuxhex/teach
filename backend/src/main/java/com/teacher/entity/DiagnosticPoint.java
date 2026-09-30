package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "diagnostic_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String purpose;

    @Builder.Default
    private Integer positions = 0;

    @Builder.Default
    private String intensity = "普通";

    @Builder.Default
    private String status = "active";

    @ManyToOne
    @JoinColumn(name = "module_id")
    private DiagnosticModule module;
}
