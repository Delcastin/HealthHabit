package com.zerobase.healthhabit.service.impl.exercise;

import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface ExerciseService {

    // 운동 목록 전체 조회하기
    Page<ExerciseCourse> getExerciseCourses(Pageable pageable);

    // 운동 Course 검색하기
    ExerciseCourse findById(Long id);

    // 운동 Course 생성하기
    ExerciseCourse createExercise(ExerciseCourseCreateRequest exercise);

    // 운동 Course 수정하기
    ExerciseCourse updateExercise(Long id, ExerciseCourseUpdateRequest exercise);

    // 운동 Course 삭제하기
    void deleteExercise(Long id);

    // 운동 시작 (session 생성)
    Long startExercise(Long userId, Long exerciseCourseId);

    // 운동 완료 (진행 중 세션 완료)
    void completeExercise(Long userId, Long sessionId) throws AccessDeniedException;


}
