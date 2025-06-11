package com.zerobase.healthhabit.service.impl;

import com.zerobase.healthhabit.dto.ChangePasswordForm;
import com.zerobase.healthhabit.dto.EditForm;
import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {


    void signUp(@Valid SignUpRequest request);

    User approveUser(Long userId);

    User getUserById(Long id);

    void updateAccountInfo(Long id, EditForm editForm);

    void updatePassword(Long id, ChangePasswordForm changePasswordForm);

    List<User> getPendingUsers();
}
