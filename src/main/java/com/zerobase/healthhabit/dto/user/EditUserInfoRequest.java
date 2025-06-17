package com.zerobase.healthhabit.dto.user;

import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserInfoRequest {

    private String username;
    private ExerciseType preferExercise;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
}
