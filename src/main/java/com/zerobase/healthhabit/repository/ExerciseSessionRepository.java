package com.zerobase.healthhabit.repository;

import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.entity.ExerciseSession;
import com.zerobase.healthhabit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExerciseSessionRepository extends JpaRepository<ExerciseSession, Long> {
    boolean existsByUserAndExerciseCourseAndCompletedFalse(User user, ExerciseCourse course);

    default ExerciseSession getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("선택된 운동 세션이 없습니다."));
    }
}
