package com.zerobase.healthhabit.dto;


import com.zerobase.healthhabit.entity.ExerciseType;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String userName;

    private UserRole role;

    private ExerciseType preferExercise;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUsername())
                .role(user.getRole())
                .preferExercise(user.getPreferExercise())
                .build();
    }
}
