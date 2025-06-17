package com.zerobase.healthhabit.dto.exercisecourse;

import com.zerobase.healthhabit.entity.ExerciseCourse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExerciseCourseResponse {

    private Long id;

    private String exerciseName;

    private LocalDate exerciseDate;

    private int durationMinutes;

    private String videoUrl;

    private Long challengeId;

    public static ExerciseCourseResponse fromExerciseCourse(ExerciseCourse exerciseCourse) {
        return ExerciseCourseResponse.builder()
                .id(exerciseCourse.getId())
                .exerciseName(exerciseCourse.getExerciseName())
                .exerciseDate(exerciseCourse.getExerciseDate())
                .durationMinutes(exerciseCourse.getDurationMinutes())
                .videoUrl(exerciseCourse.getVideoUrl())
                .build();
    }
}
