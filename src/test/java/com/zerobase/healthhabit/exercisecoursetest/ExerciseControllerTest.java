package com.zerobase.healthhabit.exercisecoursetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseCreateRequest;
import com.zerobase.healthhabit.dto.exercisecourse.ExerciseCourseUpdateRequest;
import com.zerobase.healthhabit.entity.ExerciseCourse;
import com.zerobase.healthhabit.entity.ExerciseType;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.entity.UserRole;
import com.zerobase.healthhabit.repository.ExerciseRepository;
import com.zerobase.healthhabit.repository.UserRepository;
import com.zerobase.healthhabit.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("운동 코스 생성 성공")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createExerciseSuccess() throws Exception {
        // given
        ExerciseCourseCreateRequest request = new ExerciseCourseCreateRequest();
        request.setExerciseName("유연성 20분 중급");
        request.setDurationMinutes(20);
        request.setVideoUrl("https://www.youtube.com/example/7234545kjkjas");
        request.setExerciseType(ExerciseType.STRETCH);
        request.setChallengeId(null);

        // when + then
        mockMvc.perform(post("/api/exercises/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.exerciseName").value("유연성 20분 중급"))
                .andExpect((ResultMatcher) jsonPath("$.durationMinutes").value(20));



    }

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("test1234"))
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        String token = jwtUtils.generateToken("testuser", "ROLE_USER");
    }


    @Test
    @DisplayName("운동 코스 생성 실패 - 일반 사용자 권한(403 Forbidden)")
    void createExerciseCourseFailed_Unauthorized() throws Exception {
        // given

        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("test1234"))
                .role(UserRole.USER)
                .email("testuser@example.com")
                .build();

        userRepository.save(user);

        ExerciseCourseCreateRequest request = new ExerciseCourseCreateRequest();
        request.setExerciseName("하체 30분 강화 운동");
        request.setDurationMinutes(30);
        request.setVideoUrl("https://www.youtube.com/example/7234545kjkjas");
        request.setExerciseType(ExerciseType.LOWER);

        String json = objectMapper.writeValueAsString(request);

        // 일반 사용자 토큰
        String userToken = jwtUtils.generateToken("testuser@example.com", "ROLE_USER");


        // when + then
        mockMvc.perform(post("/api/exercises/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());


    }

    @Test // 필수 입력 누락으로 인한 실패 테스트
    void createExerciseCourseFailed_MissingRequiredFields() throws Exception {
        User adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("test1234"))
                .role(UserRole.ADMIN)
                .email("testadminuser@zerobase.com")
                .build();

        userRepository.save(adminUser);

        ExerciseCourseCreateRequest request = new ExerciseCourseCreateRequest();
        request.setDurationMinutes(15);
        request.setExerciseType(ExerciseType.LOWER);
        request.setChallengeId(null);
        request.setVideoUrl("http://youtube.com/wonder/214jnF");

        String json = objectMapper.writeValueAsString(request);

        String adminToken = jwtUtils.generateToken("testadminuser@zerobase.com", "ROLE_ADMIN");

        mockMvc.perform(post("/api/exercises/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("운동 코스 수정 성공 - 관리자 권한")
    void updateExerciseCourseSuccess() throws Exception{
        // given

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("test1234"))
                .role(UserRole.ADMIN)
                .email("testadminuser@zerobase.com")
                .preferExercise(ExerciseType.CARDIO)
                .build();

        userRepository.save(admin);

        // 기존 운동 코스 지정
        ExerciseCourse savedCourse = ExerciseCourse.builder()
                .exerciseName("상체 근력 30분")
                .durationMinutes(30)
                .videoUrl("https://www.youtube.com/example/7234545kjkjas")
                .exerciseType(ExerciseType.UPPER)
                .build();

        exerciseRepository.save(savedCourse);

        // 수정 요청 DTO
        ExerciseCourseUpdateRequest updateRequest = new ExerciseCourseUpdateRequest();
        updateRequest.setExerciseName("전신 스트레칭 30분");
        updateRequest.setDurationMinutes(30);
        updateRequest.setExerciseType(ExerciseType.STRETCH);
        updateRequest.setVideoUrl("https://www.youtube.com/chest/15hjkga8");

        String json = objectMapper.writeValueAsString(updateRequest);
        String adminToken = jwtUtils.generateToken("testadminuser@zerobase.com", "ROLE_ADMIN");

        // when + then
        mockMvc.perform(put("/api/exercises/" + savedCourse.getId())
        .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseName").value("전신 스트레칭 30분"))
                .andExpect(jsonPath("$.exerciseType").value("STRETCH"))
                .andExpect(jsonPath("$.durationMinutes").value(30))
                .andExpect(jsonPath("$.videoUrl").value("https://www.youtube.com/chest/15hjkga8"));

    }

    @Test
    @DisplayName("운동 코스 삭제 성공 - 관리자 권한")
    void deleteExerciseCourseSuccess() throws Exception{
        // given
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("test1234"))
                .role(UserRole.ADMIN)
                .email("testadminuser@zerobase.com")
                .preferExercise(ExerciseType.CARDIO)
                .build();
        userRepository.save(admin);

        ExerciseCourse course = ExerciseCourse.builder()
                .exerciseName("상체 근력 30분")
                .durationMinutes(30)
                .videoUrl("https://www.youtube.com/example/7234545kjkjas")
                .exerciseType(ExerciseType.UPPER)
                .build();
        exerciseRepository.save(course);

        String token = jwtUtils.generateToken(admin.getEmail(), "ROLE_ADMIN");

        // when + then
        mockMvc.perform(delete("/api/exercises/{id}", course.getId())
        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

    }
}
