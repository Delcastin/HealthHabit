package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.challenge.ChallengeCreateRequest;
import com.zerobase.healthhabit.dto.challenge.ChallengeResponse;
import com.zerobase.healthhabit.service.impl.challenge.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping("/user/{userId}/challenges") // 챌린지 생성
    public ResponseEntity<ChallengeResponse> createChallenge(
            @PathVariable("userId") Long userId,
            @RequestBody ChallengeCreateRequest request){

        ChallengeResponse response = challengeService.createChallenge(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/challenges") // 사용자 챌린지 목록 조회
    public ResponseEntity<List<ChallengeResponse>> getChallenges(
            @PathVariable("userId") Long userId,
            @PathVariable("challengeId") Long challengeId){

        ChallengeResponse response = challengeService.getChallenge(userId, challengeId);
        return ResponseEntity.ok(Collections.singletonList(response));
    }

    // 특정 챌린지 조회
    @GetMapping("/users/{userId}/challenges/{challengeId}")
    public ResponseEntity<ChallengeResponse> getChallenge(
            @PathVariable Long userId,
            @PathVariable Long challengeId) {

        ChallengeResponse response = challengeService.getChallenge(userId, challengeId);
        return ResponseEntity.ok(response);
    }
}
