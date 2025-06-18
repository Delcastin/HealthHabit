package com.zerobase.healthhabit.dto.exercisecourse;

import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ExerciseCourseSearchRequest {

    private String keyword; // 제목 검색
    private ExerciseType exerciseType; // 운동 종류 검색
    private int minDurationMinutes; // 최소 시간 필터 검색
    private int maxDurationMinutes; // 최대 시간 필터 검색

}
