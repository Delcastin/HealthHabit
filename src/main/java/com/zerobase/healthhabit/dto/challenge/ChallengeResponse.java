package com.zerobase.healthhabit.dto.challenge;


import com.zerobase.healthhabit.entity.ExerciseLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeResponse {

    private Long challengeId;
    private String challengeName;
    private int totalScore;
    private int rewardAmount;
    private boolean isCompleted;

    private ExerciseLevel exerciseLevel;
}
