package com.zerobase.healthhabit.service.impl.exercise;

import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;

import java.util.List;

public interface ExerciseService {

    // 운동 목록 전체 조회하기
    List<ExerciseCourse> findAll();

    // 운동 Course 검색하기
    ExerciseCourse findById(Long id);

    // 운동 Course 생성하기
    ExerciseCourse createExercise(ExerciseCourseCreateRequest exercise);

    // 운동 Course 수정하기
    ExerciseCourse updateExercise(Long id, ExerciseCourseUpdateRequest exercise);

    // 운동 Course 삭제하기
    void deleteExercise(Long id);


}
