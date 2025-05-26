package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ExercisePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Challenge challenge;

    private LocalDate exerciseDate;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    private int durationMinutes;

    private String videoUrl;
}
