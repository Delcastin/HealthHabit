package com.zerobase.healthhabit.repository;


import com.zerobase.healthhabit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email); // 이메일 조회

    boolean existsByEmail(String email); // 이메일이 중복되는지 확인 후 결과 알림.

    Optional<User> findById(Long id); // 사용자 ID 조회

    Optional<User> findByUsername(String username); // 사용자 이름 조회
}
