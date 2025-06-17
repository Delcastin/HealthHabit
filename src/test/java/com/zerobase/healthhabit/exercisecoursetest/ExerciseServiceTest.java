package com.zerobase.healthhabit.exercisecoursetest;


import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.entity.ExerciseSession;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.repository.ExerciseSessionRepository;
import com.zerobase.healthhabit.repository.UserRepository;
import com.zerobase.healthhabit.service.impl.exercise.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ExerciseServiceTest {

    private ExerciseServiceImpl exerciseService;
    private UserRepository userRepository;
    private ExerciseRepository exerciseRepository;
    private ExerciseSessionRepository exerciseSessionRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        exerciseRepository = mock(ExerciseRepository.class);
        exerciseSessionRepository = mock(ExerciseSessionRepository.class);
        exerciseService = new ExerciseServiceImpl(exerciseRepository, userRepository, exerciseSessionRepository);
    }

    @Test
    @DisplayName("운동 시작 - 성공")
    void exerciseStart_Succuess(){
        // Given
        Long userId = 1L;
        Long exerciseId = 10L;
        User user = new User(); user.setId(userId);
        ExerciseCourse course = new ExerciseCourse(); course.setId(exerciseId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(course));
        when(exerciseSessionRepository.existsByUserAndExerciseCourseAndCompletedFalse(user, course)).thenReturn(false);

        ExerciseSession exerciseSession = ExerciseSession.builder()
                .user(user)
                .exerciseCourse(course)
                .startedAt(LocalDateTime.now())
                .completed(false)
                .sessionId(100L)
                .build();

        when(exerciseSessionRepository.save(any(ExerciseSession.class)))
                .thenAnswer(invocation -> {
                    ExerciseSession inputSession = invocation.getArgument(0);
                    inputSession.setSessionId(100L);
                    return inputSession;
                });


        // when

        Long sessionId = exerciseService.startExercise(userId, exerciseId);

        // then

        assertNotNull(sessionId);
        assertEquals(100L, exerciseSession.getSessionId());
    }

    @DisplayName("운동 시작 실패 - 이미 진행 중인 세션이 있음")
    @Test
    void exerciseStart_Fail_AlreadyInProgress() {
        // given
        Long userId = 1L;
        Long courseId = 10L;
        User user = User.builder().id(userId).build();
        ExerciseCourse course = ExerciseCourse.builder().id(courseId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(exerciseSessionRepository.existsByUserAndExerciseCourseAndCompletedFalse(user, course)).thenReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> exerciseService.startExercise(userId, courseId)
        );

        assertEquals("이미 진행 중인 Course가 있습니다.", exception.getMessage());
    }

    @DisplayName("운동 시작 실패 - 존재하지 않는 사용자")
    @Test
    void exerciseStart_Fail_UserNotFound() {
        // given
        Long userId = 1L;
        Long courseId = 10L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> exerciseService.startExercise(userId, courseId)
        );

        assertEquals("사용자가 존재하지 않습니다.", exception.getMessage());
    }

    @DisplayName("운동 시작 실패 - 존재하지 않는 운동 코스")
    @Test
    void exerciseStart_Fail_CourseNotFound() {
        // given
        Long userId = 1L;
        Long courseId = 10L;
        User user = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(exerciseRepository.findById(courseId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> exerciseService.startExercise(userId, courseId)
        );

        assertEquals("운동 목록이 존재하지 않습니다.", exception.getMessage());
    }

    // 운동 완료

    @DisplayName("운동 완료 - 성공")
    @Test
    void exerciseComplete_Success() {
        Long userId = 1L;
        Long sessionId = 100L;
        User user = User.builder().id(userId).build();
        ExerciseCourse course = ExerciseCourse.builder().id(10L).build();

        ExerciseSession session = ExerciseSession.builder()
                .sessionId(sessionId)
                .user(user)
                .completed(false)
                .exerciseCourse(course)
                .startedAt(LocalDateTime.now().minusMinutes(30))
                .build();

        when(exerciseSessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // when

        exerciseService.completeExercise(userId, sessionId);

        // then

        assertTrue(session.isCompleted());
        assertNotNull(session.getEndedAt());
        verify(exerciseSessionRepository, times(1)).save(session);

    }

    @DisplayName("운동 완료 실패 - sessionId가 존재하지 않을 때")
    @Test
    void exerciseComplete_fail_whenSessionNotFound() {
        // given
        Long userId = 1L;
        Long invalidSessionId = 99L;

        given(exerciseSessionRepository.findById(invalidSessionId))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> exerciseService.completeExercise(userId, invalidSessionId));
    }

    @DisplayName("운동 완료 실패 - 이미 완료된 세션일 때")
    @Test
    void exerciseComplete_fail_whenAlreadyCompleted() {
        // given
        Long userId = 1L;
        Long sessionId = 10L;
        User user = User.builder().id(userId).build();

        ExerciseSession session = ExerciseSession.builder()
                .sessionId(sessionId)
                .user(user)
                .completed(true) // 이미 완료됨
                .build();

        given(exerciseSessionRepository.findById(sessionId))
                .willReturn(Optional.of(session));

        // when & then
        assertThrows(IllegalStateException.class,
                () -> exerciseService.completeExercise(userId, sessionId));
    }


}
