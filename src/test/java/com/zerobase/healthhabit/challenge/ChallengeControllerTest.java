package com.zerobase.healthhabit.challenge;

import com.zerobase.healthhabit.configuration.SecurityConfig;
import com.zerobase.healthhabit.controller.ChallengeController;
import com.zerobase.healthhabit.dto.challenge.ChallengeResponse;

import com.zerobase.healthhabit.service.impl.challenge.ChallengeService;
import com.zerobase.healthhabit.utils.JwtAuthenticationFilter;
import com.zerobase.healthhabit.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChallengeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class ChallengeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChallengeService challengeService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        // JWT 토큰 검증 항상 통과
        when(jwtUtils.validateToken(anyString())).thenReturn(true);
        // 토큰에서 사용자 이름 리턴
        when(jwtUtils.extractEmail(anyString())).thenReturn("testUser");

        // UserDetailsService가 testUser 반환하도록 설정 (ROLE_USER 권한 포함)
        UserDetails userDetails = User.withUsername("testUser")
                .password("password")
                .roles("USER")
                .build();
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
    }

    @Test
    void createChallenge_success() throws Exception {
        ChallengeResponse response = ChallengeResponse.builder()
                .challengeId(1L)
                .challengeName("새로운 챌린지")
                .totalScore(10)
                .rewardAmount(10000)
                .isCompleted(false)
                .build();

        when(challengeService.createChallenge(anyLong(), any())).thenReturn(response);

        String json = """
            {
              "challengeName":"새로운 챌린지",
              "durationMinutes":30,
              "periodInMonths":2,
              "exerciseType":"STRETCH",
              "exerciseLevel":"MEDIUM"
            }
            """;

        mockMvc.perform(post("/api/user/1/challenges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer dummy-token"))  // JWT 토큰 헤더 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challengeId").value(1))
                .andExpect(jsonPath("$.challengeName").value("새로운 챌린지"));
    }
}

