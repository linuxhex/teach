package com.teacher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitAnswerResponse {
    private Integer score;
    private Integer total;
    private Integer correctCount;
    private Long resultId;
}
