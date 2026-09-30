package com.teacher.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitAnswerRequest {
    private Long testId;
    private List<Integer> answers;
    private Integer elapsed;
}
