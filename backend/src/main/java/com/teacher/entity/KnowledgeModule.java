package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "knowledge_modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String icon;

    private String color;

    @Builder.Default
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<KnowledgePoint> children;
}
