package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.service.impl.exercise.ExerciseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseRepository exerciseRepository;


    public ExerciseController(ExerciseService exerciseService, ExerciseRepository exerciseRepository) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
    }

    // 운동 목록 조회하기
    @GetMapping("/list")
    public List<ExerciseCourse> viewExerciseCourseList(){
        return exerciseService.findAll();
    }

    // 운동 Course 생성하기

    @PostMapping("/create")
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
}
