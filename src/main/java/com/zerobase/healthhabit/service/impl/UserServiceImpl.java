package com.zerobase.healthhabit.service.impl;

import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.entity.UserRole;
import com.zerobase.healthhabit.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Builder
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("해당 이메일 사용시 중복됩니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountHolder(request.getAccountHolder())
                .preferExercise(request.getPreferExercise())
                .role(UserRole.USER) // 관리자 승인 절차를 위해 일반 사용자로 무조건으로 정의한다.
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }

    @Override
    public User approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        user.setRole(UserRole.ADMIN);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
