package com.zerobase.healthhabit.dto.exercisecourse;

import com.zerobase.healthhabit.entity.ExerciseType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseCourseCreateRequest {

    @NotBlank(message = "운동 이름 입력은 필수입니다.")
    private String exerciseName;

    private LocalDate exerciseDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "운동 타입 입력은 필수입니다.")
    private ExerciseType exerciseType;

    @Min(15)
    @NotNull(message = "운동 전체 시간 입력은 필수입니다.")
    private int durationMinutes;


    @NotNull(message = "운동 영상 ID 입력은 필수입니다.")
    private Long exerciseVideoId;

    private Long challengeId;
}
