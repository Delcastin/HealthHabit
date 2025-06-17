package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.user.UserResponse;
import com.zerobase.healthhabit.service.impl.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;



    @PostMapping("/approve/{userId}")
    public ResponseEntity<UserResponse> approveUser(@PathVariable Long userId) {
        UserResponse approvedUser = userService.approveUser(userId);
        return ResponseEntity.ok(approvedUser);
    }
}
