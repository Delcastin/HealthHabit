package com.zerobase.healthhabit.dto.challenge;

import com.zerobase.healthhabit.entity.ExerciseLevel;
import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeCreateRequest {

    private String challengeName;
    private int durationMinutes;
    private int periodInMonths;
    private ExerciseType exerciseType;

    private ExerciseLevel exerciseLevel;
}
