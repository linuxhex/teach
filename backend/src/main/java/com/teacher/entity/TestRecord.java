package com.teacher.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "test_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mode;

    private Integer score;

    private Integer total;

    @Builder.Default
    private Integer correctCount = 0;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TestAnswer> answers;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
