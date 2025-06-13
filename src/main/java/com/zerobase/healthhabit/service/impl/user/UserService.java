package com.zerobase.healthhabit.service.impl.user;


import com.zerobase.healthhabit.dto.user.ChangePasswordRequest;
import com.zerobase.healthhabit.dto.user.EditUserInfoRequest;
import com.zerobase.healthhabit.dto.user.SignUpRequest;
import com.zerobase.healthhabit.dto.user.UserResponse;
import jakarta.validation.Valid;

public interface UserService {

    void signUp(@Valid SignUpRequest request); // 사용자 회원가입

    UserResponse approveUser(Long userId);      // 관리자 승인하기

    UserResponse getUserById(Long id);          // id로 사용자 조회하기

    void updateAccountInfo(Long id, EditUserInfoRequest editForm); // 내 정보 수정하기(비밀번호 제외)

    void updatePassword(Long id, ChangePasswordRequest changePasswordForm); // 사용자 비밀번호 변경

    // 엔티티 → DTO 리스트
}
