package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.UserResponse;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PatchMapping("/approve/{userId}")
    public ResponseEntity<UserResponse> approveUser(@PathVariable Long userId) {
        User approvedUser = userService.approveUser(userId);
        return ResponseEntity.ok(UserResponse.from(approvedUser));
    }
}
