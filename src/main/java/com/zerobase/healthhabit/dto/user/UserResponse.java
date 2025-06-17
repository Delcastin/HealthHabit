package com.zerobase.healthhabit.dto.user;


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
    private String username;

    private UserRole role;

    private ExerciseType preferExercise;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .preferExercise(user.getPreferExercise())
                .build();
    }
}
