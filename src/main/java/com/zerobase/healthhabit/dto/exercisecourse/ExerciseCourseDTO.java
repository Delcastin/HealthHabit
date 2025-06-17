package com.zerobase.healthhabit.dto.exercisecourse;

import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ExerciseCourseDTO {

    private Long id;
    private String exerciseName;
    private String videoUrl;
    private String videoTitle;
    private int durationMinutes;
    private ExerciseType exerciseType;

    public static ExerciseCourseDTO fromDto(ExerciseCourse exerciseCourse) {
        return ExerciseCourseDTO.builder()
                .id(exerciseCourse.getId())
                .exerciseName(exerciseCourse.getExerciseName())
                .videoUrl(
                        exerciseCourse.getExerciseVideo() != null
                                ? exerciseCourse.getExerciseVideo().getYoutubeUrl()
                                : null
                )
                .durationMinutes(exerciseCourse.getDurationMinutes())
                .exerciseType(exerciseCourse.getExerciseType())
                .build();
    }

}
