package com.zerobase.healthhabit.service.impl.challenge;

import com.zerobase.healthhabit.dto.challenge.ChallengeCreateRequest;
import com.zerobase.healthhabit.dto.challenge.ChallengeResponse;

import java.util.List;


public interface ChallengeService {

    ChallengeResponse createChallenge(Long userId, ChallengeCreateRequest request); // Challenge 생성

    void completeChallenge(Long userId, Long challengeId); // Challenge 완료

    List<ChallengeResponse> getUserChallenges(Long userId); // Challenge 조회

    ChallengeResponse getChallenge(Long userId, Long challengeId); // Challenge 상세조회
}
