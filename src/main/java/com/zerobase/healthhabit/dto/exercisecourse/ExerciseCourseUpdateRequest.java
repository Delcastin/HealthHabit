package com.zerobase.healthhabit.dto.exercisecourse;


import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseCourseUpdateRequest {

    private String exerciseName;
    private LocalDate exerciseDate;

    private ExerciseType exerciseType;
    private Integer durationMinutes;

    private Long exerciseVideoId;
}
