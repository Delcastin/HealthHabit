package com.zerobase.healthhabit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ChallengeVideo {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Challenge challenge;

    @ManyToOne
    private ExerciseVideo exerciseVideo;

}
