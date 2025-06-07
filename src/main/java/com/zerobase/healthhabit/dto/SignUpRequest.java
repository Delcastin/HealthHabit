package com.zerobase.healthhabit.dto;

import com.zerobase.healthhabit.entity.ExerciseType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private ExerciseType preferExercise;

    private String bankName;
    private String accountNumber;
    private String accountHolder;

}
