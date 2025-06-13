package com.zerobase.healthhabit.service.impl.user;

import com.zerobase.healthhabit.dto.user.ChangePasswordRequest;
import com.zerobase.healthhabit.dto.user.EditUserInfoRequest;
import com.zerobase.healthhabit.dto.user.SignUpRequest;
import com.zerobase.healthhabit.dto.user.UserResponse;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.entity.UserRole;
import com.zerobase.healthhabit.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("해당 이메일은 이미 사용 중입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .bankName(request.getBankName())
                .accountNumber(request.getAccountNumber())
                .accountHolder(request.getAccountHolder())
                .preferExercise(request.getPreferExercise())
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    @Override
    public UserResponse approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

        if(user.getRole() == UserRole.ADMIN) {
            throw new IllegalStateException("이미 관리자로 승인된 사용자입니다.");
        }

        user.setRole(UserRole.ADMIN);
        return UserResponse.from(userRepository.save(user));
    }


    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        return UserResponse.from(user);
    }

    @Override
    public void updateAccountInfo(Long userId, EditUserInfoRequest editForm) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        user.setBankName(editForm.getBankName());
        user.setAccountNumber(editForm.getAccountNumber());
        user.setAccountHolder(editForm.getAccountHolder());
        user.setPreferExercise(editForm.getPreferExercise());
        user.setUsername(editForm.getUsername());

        userRepository.save(user);
    }

    @Override
    public void updatePassword(Long userId, ChangePasswordRequest form) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        userRepository.save(user);
    }


}
