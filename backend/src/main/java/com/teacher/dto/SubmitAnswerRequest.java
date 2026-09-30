package com.teacher.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitAnswerRequest {
    private List<AnswerItem> answers;
    private Integer elapsed;

    @Data
    public static class AnswerItem {
        private Long questionId;
        private Integer answer;
    }
}
