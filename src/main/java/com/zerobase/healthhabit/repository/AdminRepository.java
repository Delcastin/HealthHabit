package com.zerobase.healthhabit.repository;

import com.zerobase.healthhabit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, Long> {
}
