package com.zerobase.healthhabit.repository;

import com.zerobase.healthhabit.entity.ExerciseCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<ExerciseCourse, Long> {
}
