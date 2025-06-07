package com.zerobase.healthhabit.service.impl;

import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.entity.User;
import jakarta.validation.Valid;

public interface UserService {


    void signUp(@Valid SignUpRequest request);

    User approveUser(Long userId);

    User getUserById(Long id);
}
