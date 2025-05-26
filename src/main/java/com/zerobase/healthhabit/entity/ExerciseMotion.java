package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;

@Entity
public class ExerciseMotion {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ExerciseType type;
}
