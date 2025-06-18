package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.exercisecourse.*;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.service.impl.UserDetailsImpl;
import com.zerobase.healthhabit.service.impl.exercise.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;


    // 운동 목록 조회하기
    @GetMapping
    public ExerciseCourseListResponse<ExerciseCourseResponse> viewExerciseCourseList(Pageable pageable,
                                                                                        @ModelAttribute @Valid ExerciseCourseSearchRequest searchRequest) {
        Page<ExerciseCourseResponse> page = exerciseService.getExerciseCourses(pageable);

        return new ExerciseCourseListResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }


    // 운동 Course 생성하기

    @PostMapping
    public ResponseEntity<?> editExerciseCourse(@RequestBody @Valid ExerciseCourseCreateRequest request,
                                                Authentication authentication) {
        ExerciseCourse course = exerciseService.createExercise(request);
        return ResponseEntity.ok(course);

    }

    // 운동 Course 수정하기

    @PutMapping("/{id}")
    public ResponseEntity<?> updateExerciseCourse(@RequestBody ExerciseCourseUpdateRequest updateRequest,
                                               @PathVariable("id") Long id){

        ExerciseCourse savedCourse = exerciseService.updateExercise(id, updateRequest);
        return ResponseEntity.ok(savedCourse);
    }

    // 운동 Course 삭제하기

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable("id") Long id){
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    // 운동 목록 상세조회
    @GetMapping("/{id}")
    public ExerciseCourseDTO viewExerciseCourse(@PathVariable("id") Long courseId){
        ExerciseCourse course = exerciseService.findById(courseId);
        return ExerciseCourseDTO.fromDto(course);
    }

    // 운동 시작
    @PostMapping("/start/{exerciseCourseId}")
    public ResponseEntity<ExerciseCourseStartResponse> startExerciseCourse(
            @PathVariable("exerciseCourseId") Long exerciseCourseId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long sessionId = exerciseService.startExercise(userDetails.getId(), exerciseCourseId);
        return ResponseEntity.ok(new ExerciseCourseStartResponse(exerciseCourseId, LocalDateTime.now(), sessionId));
    }

    // 운동 완료
    @PostMapping("/complete/{sessionId}")
    public ResponseEntity<String> completeExerciseCourse(
            @PathVariable("sessionId") Long sessionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        exerciseService.completeExercise(userDetails.getId(), sessionId);
        return ResponseEntity.ok("운동이 완료되었습니다.");
    }

}
