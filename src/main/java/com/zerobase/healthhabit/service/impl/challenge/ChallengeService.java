package com.zerobase.healthhabit.service.impl.challenge;

import com.zerobase.healthhabit.dto.challenge.ChallengeCreateRequest;
import com.zerobase.healthhabit.dto.challenge.ChallengeResponse;


public interface ChallengeService {

    ChallengeResponse createChallenge(Long userId, ChallengeCreateRequest request); // Challenge 생성
}
