package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String type;

    private String purpose;

    private String stage;

    private String version;

    private String volume;

    private String chapter;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String audience;

    @Column(columnDefinition = "TEXT")
    private String tags;

    private BigDecimal price;

    private BigDecimal originalPrice;

    @Builder.Default
    private Integer matchRate = 0;
}
