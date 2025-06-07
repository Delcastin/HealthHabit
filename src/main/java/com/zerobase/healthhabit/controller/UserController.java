package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.dto.UserResponse;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.service.impl.UserDetailsImpl;
import com.zerobase.healthhabit.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignup(@RequestBody @Valid SignUpRequest request) {

        userService.signUp(request);

        return ResponseEntity.ok("회원가입이 정상적으로 이루어졌습니다.");
    }
    
    @GetMapping("/info")
    public ResponseEntity<UserResponse> getMyPageInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(UserResponse.from(user));
    }

}
