package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String exerciseName; // 코스 이름

    @ManyToOne
    private Challenge challenge; // 챌린지

    private LocalDate exerciseDate; // 운동 날짜

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType; // 운동 타입

    private int durationMinutes; // 코스 1세트당 시간

    private String videoUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "exercise_video_id")
    private ExerciseVideo exerciseVideo;

    @Enumerated(EnumType.STRING)
    private ExerciseLevel exerciseLevel;  // EASY, MEDIUM, HARD

}
