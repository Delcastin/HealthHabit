package com.zerobase.healthhabit.entity;

import lombok.Getter;

@Getter
public enum ExerciseLevel {
    EASY(2),
    MEDIUM(4),
    HARD(6);

    private final int score;

    ExerciseLevel(int score) {
        this.score = score;
    }

}
