package com.zerobase.healthhabit.repository;

import com.zerobase.healthhabit.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
