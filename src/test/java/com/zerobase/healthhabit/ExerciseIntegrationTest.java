package com.zerobase.healthhabit;

import com.zerobase.healthhabit.entity.*;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.repository.ExerciseSessionRepository;
import com.zerobase.healthhabit.repository.ExerciseVideoRepository;
import com.zerobase.healthhabit.repository.UserRepository;
import com.zerobase.healthhabit.service.impl.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExerciseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    ExerciseSessionRepository exerciseSessionRepository;

    private User testUser;
    private ExerciseCourse testCourse;

    @Autowired
    private ExerciseVideoRepository exerciseVideoRepository;

    private ExerciseVideo testVideo;

    @BeforeEach
    void setUp(){
        // 사용자 저장
        testUser = userRepository.save(User.builder()
                        .username("testUser")
                        .password("testPassword")
                        .preferExercise(ExerciseType.STRENGTH)
                        .role(UserRole.USER)
                .build());

        testVideo = exerciseVideoRepository.save(ExerciseVideo.builder()
                        .videoName("testVideo")
                        .durationMinutes(20)
                        .exerciseLevel(ExerciseLevel.HARD)
                .build());

        // 운동 코스 저장
        testCourse = exerciseRepository.save(ExerciseCourse.builder()
                        .exerciseName("StrengthTwenty")
                        .exerciseType(ExerciseType.STRENGTH)
                        .durationMinutes(20)
                        .exerciseVideo(testVideo)
                .build());
    }

    @Test
    @DisplayName("운동 시작 및 완료 통합 테스트 성공")
    void startAndCompleteExercise() throws Exception {
        // 1. 테스트 사용자 생성
        User testUser = new User();
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
        testUser.setRole(UserRole.USER);
        userRepository.save(testUser);

        // 2. 인증 객체 생성 및 SecurityContext에 등록
        UserDetailsImpl userDetails = new UserDetailsImpl(testUser); // 여러분의 커스텀 UserDetails 구현체

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 운동 시작
        mockMvc.perform(post("/api/exercises/start/{id}", testCourse.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"exerciseId\":" + testCourse.getId() + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").exists());

        // 4. 세션이 생성되었는지 확인
        ExerciseSession session = exerciseSessionRepository.findAll().getFirst();
        assertThat(session.isCompleted()).isFalse();

        // 5. 운동 완료
        mockMvc.perform(post("/api/exercises/complete/{id}", testCourse.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ex\":" + session.getSessionId() + "}"))
                .andExpect(status().isOk());

        // 6. 완료 상태 확인
        ExerciseSession updated = exerciseSessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
        assertThat(updated.getEndedAt()).isNotNull();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

}
