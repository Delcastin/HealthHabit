package com.zerobase.healthhabit.service.impl.challenge;

import com.zerobase.healthhabit.dto.challenge.ChallengeCreateRequest;
import com.zerobase.healthhabit.dto.challenge.ChallengeResponse;
import com.zerobase.healthhabit.entity.Challenge;
import com.zerobase.healthhabit.entity.ExerciseLevel;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.repository.ChallengeRepository;
import com.zerobase.healthhabit.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional
    public ChallengeResponse createChallenge(Long userId, ChallengeCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자가 없습니다."));

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusMonths(request.getPeriodInMonths());

        int durationScore = getDurationScore(request.getDurationMinutes());
        int periodScore = getPeriodScore(startDate, endDate);
        int levelScore = request.getExerciseLevel().getScore();
        int totalScore = durationScore + periodScore + levelScore;
        int rewardAmount = calculateReward(totalScore);

        Challenge challenge = Challenge.builder()
                .user(user)
                .challengeName(request.getChallengeName())
                .startDate(startDate)
                .endDate(endDate)
                .durationMinutes(request.getDurationMinutes())
                .level(request.getExerciseLevel())
                .exerciseType(request.getExerciseType())
                .durationScore(durationScore)
                .periodScore(periodScore)
                .levelScore(levelScore)
                .totalScore(totalScore)
                .rewardAmount(rewardAmount)
                .isCompleted(false)
                .build();

        Challenge saved = challengeRepository.save(challenge);

        return ChallengeResponse.builder()
                .challengeId(saved.getId())
                .challengeName(saved.getChallengeName())
                .totalScore(saved.getTotalScore())
                .rewardAmount(saved.getRewardAmount())
                .isCompleted(saved.getIsCompleted())
                .build();
    }

    private int getDurationScore(int minutes) {
        if (minutes >= 40) return 8;
        if (minutes >= 30) return 6;
        if (minutes >= 20) return 4;
        if (minutes >= 15) return 2;
        return 0;
    }

    private int getPeriodScore(LocalDate start, LocalDate end) {
        long months = ChronoUnit.MONTHS.between(start.withDayOfMonth(1), end.withDayOfMonth(1));
        if (months >= 4) return 7;
        if (months == 3) return 5;
        if (months == 2) return 4;
        if (months == 1) return 1;
        return 0;
    }

    private int calculateReward(int totalScore) {
        if (totalScore >= 18) return 180_000;
        if (totalScore >= 13) return 120_000;
        if (totalScore >= 7) return 60_000;
        return 0;
    }

    private int getLevelScore(ExerciseLevel level) {
        return level.getScore();
    }
}
