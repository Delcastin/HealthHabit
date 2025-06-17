package com.zerobase.healthhabit.dto.challenge;

import com.zerobase.healthhabit.entity.ExerciseLevel;
import com.zerobase.healthhabit.entity.ExerciseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeCreateRequest {

    @NotBlank
    private String challengeName;

    @Min(15)
    private int durationMinutes;

    @Min(1)
    private int periodInMonths;

    @NotNull
    private ExerciseType exerciseType;

    @NotNull
    private ExerciseLevel exerciseLevel;
}
