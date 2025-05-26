package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ExerciseLevel level;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    private int durationMinutes;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    private Integer durationScore; // 시간 기준 점수
    private Integer periodScore; // 기간 기준 점수
    private Integer levelScore; // 난이도 기준 점수
    private Integer totalScore; // 총 점수

    private Integer rewardAmount; // 보상 금액

    private Boolean isCompleted; // 챌린지 완료 여부
}
