package com.zerobase.healthhabit.service.impl.exercise;


import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.entity.ExerciseSession;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.repository.ExerciseSessionRepository;
import com.zerobase.healthhabit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ExerciseSessionRepository exerciseSessionRepository;


    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserRepository userRepository, ExerciseSessionRepository exerciseSessionRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.exerciseSessionRepository = exerciseSessionRepository;
    }

    @Override
    public Page<ExerciseCourse> getExerciseCourses(Pageable pageable) {
        return exerciseRepository.findAll(pageable);
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

    @Override
    public Long startExercise(Long userId, Long exerciseId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        ExerciseCourse course = exerciseRepository.findById(exerciseId).orElseThrow(
                () -> new RuntimeException("운동 목록이 존재하지 않습니다.")
        );

        boolean exists = exerciseSessionRepository.existsByUserAndExerciseCourseAndCompletedFalse(user, course);
        if (exists) {
            throw new IllegalArgumentException("이미 진행 중인 Course가 있습니다.");
        }

        ExerciseSession session = ExerciseSession.builder()
                .user(user)
                .exerciseCourse(course)
                .startedAt(LocalDateTime.now())
                .completed(false)
                .build();

        exerciseSessionRepository.save(session);
        return session.getSessionId();
    }

    @Override
    public void completeExercise(Long userId, Long sessionId) throws AccessDeniedException {
        ExerciseSession session = exerciseSessionRepository.findById(sessionId).orElseThrow(
                () -> new IllegalArgumentException("선택된 운동 코스가 없습니다.")
        );

        if (!session.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("본인의 운동 코스만 완료가 가능합니다.");
        }

        if (session.isCompleted()) {
            throw new IllegalStateException("이미 완료된 코스입니다.");
        }

        session.setCompleted(true);
        session.setEndedAt(LocalDateTime.now());
        exerciseSessionRepository.save(session);
    }


}
