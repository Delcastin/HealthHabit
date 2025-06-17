package com.zerobase.healthhabit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseVideo { // 유튜브 운동 영상 담당 Entity Class

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoName;
    private String youtubeUrl;

    @Enumerated(EnumType.STRING)
    private ExerciseLevel exerciseLevel;

    private int durationMinutes;

    @ManyToMany
    @JoinTable(name = "video_motion",
    joinColumns = @JoinColumn(name = "video_id"),
    inverseJoinColumns = @JoinColumn(name = "motion_id"))
    private List<ExerciseMotion> motions = new ArrayList<>();

    @OneToOne(mappedBy = "exerciseVideo")
    private ExerciseCourse exerciseCourse;
}
