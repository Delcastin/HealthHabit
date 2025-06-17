package com.zerobase.healthhabit.repository;

import com.zerobase.healthhabit.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByUserId(Long userId);

}
