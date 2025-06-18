package com.zerobase.healthhabit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExerciseSession {

    @Id
    @GeneratedValue
    private Long sessionId;

    @ManyToOne
    private User user; // 사용자

    @ManyToOne
    private ExerciseCourse exerciseCourse; // 선택한 운동 코스

    private LocalDateTime startedAt; // 운동 코스를 시작한 시각
    private LocalDateTime endedAt; // 운동 코스를 완료한 시각

    private boolean completed; // 완료 여부
}
