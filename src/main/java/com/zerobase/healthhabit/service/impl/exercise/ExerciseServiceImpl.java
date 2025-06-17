package com.zerobase.healthhabit.service.impl.exercise;


import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<ExerciseCourse> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public ExerciseCourse findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise Course Were Not Found."));
    }

    @Override
    public ExerciseCourse createExercise(ExerciseCourseCreateRequest request) {

        ExerciseCourse course = ExerciseCourse.builder()
                .exerciseName(request.getExerciseName())
                .exerciseType(request.getExerciseType())
                .exerciseDate(request.getExerciseDate())
                .videoUrl(request.getVideoUrl())
                .durationMinutes(request.getDurationMinutes())
                .build();

        return exerciseRepository.save(course);
    }

    @Override
    public ExerciseCourse updateExercise(Long id, ExerciseCourseUpdateRequest exercise) {
        ExerciseCourse existingCourse = findById(id);
        existingCourse.setDurationMinutes(exercise.getDurationMinutes());
        existingCourse.setExerciseType(exercise.getExerciseType());
        existingCourse.setVideoUrl(exercise.getVideoUrl());
        existingCourse.setExerciseName(exercise.getExerciseName());



        return exerciseRepository.save(existingCourse);
    }

    @Override
    public void deleteExercise(Long id) {
        ExerciseCourse existingCourse = findById(id);
        exerciseRepository.delete(existingCourse);
    }
}
