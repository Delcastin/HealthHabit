package com.zerobase.healthhabit.dto.exercisecourse;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseCourseStartResponse {

    private Long exerciseId; // 요청된 운동 목록 ID
    private LocalDateTime startedAt; // 운동 시작일시
    private Long sessionId; // 요청 고유 ID
}
