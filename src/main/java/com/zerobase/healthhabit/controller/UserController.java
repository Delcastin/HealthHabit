package com.zerobase.healthhabit.controller;



import com.zerobase.healthhabit.dto.user.ChangePasswordRequest;
import com.zerobase.healthhabit.dto.user.EditUserInfoRequest;
import com.zerobase.healthhabit.dto.user.SignUpRequest;
import com.zerobase.healthhabit.dto.user.UserResponse;
import com.zerobase.healthhabit.service.impl.UserDetailsImpl;
import com.zerobase.healthhabit.service.impl.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/signup") // 회원가입
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        try {
            userService.signUp(signUpRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/info")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserResponse user = userService.getUserById(userDetails.getId());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit") // 내 정보 수정하기
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody EditUserInfoRequest request) {
        userService.updateAccountInfo(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/edit/password") // 내 비밀번호 변경하기
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody ChangePasswordRequest request) {
        try {
            userService.updatePassword(userDetails.getId(), request);
            return ResponseEntity.ok("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }






}
