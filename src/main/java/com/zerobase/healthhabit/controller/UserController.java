package com.zerobase.healthhabit.controller;


import co.elastic.clients.elasticsearch.security.ChangePasswordRequest;
import com.zerobase.healthhabit.dto.ChangePasswordForm;
import com.zerobase.healthhabit.dto.EditForm;
import com.zerobase.healthhabit.dto.SignUpRequest;
import com.zerobase.healthhabit.dto.UserResponse;
import com.zerobase.healthhabit.entity.ExerciseType;
import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.service.impl.UserDetailsImpl;
import com.zerobase.healthhabit.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/signup")
    public String userSignup(Model model) {
        model.addAttribute("signupForm", new SignUpRequest());
        model.addAttribute("exerciseType", ExerciseType.values());// ← 중요!
        return "signup";  // templates/signup.html 렌더링
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("signupForm") @Valid SignUpRequest signUpRequest,
                                BindingResult bindingResult,
                                Model model) {
        // 유효성 검사 실패 시 다시 회원가입 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            return "signup";  // templates/signup.html 렌더링
        }

        try {
            userService.signUp(signUpRequest);  // 회원가입 로직 처리
        } catch (Exception e) {
            model.addAttribute("signupError", e.getMessage());
            return "signup";
        }

        return "redirect:/user/login";  // 가입 성공하면 로그인 페이지로 리다이렉트
    }


    @GetMapping("/info") // 사용자 - 내 정보 조회하기
    public String getMyPageInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userService.getUserById(userDetails.getId());
        model.addAttribute("user", user);
        return "info";
    }

    @GetMapping("/edit") // 내 정보 수정화면 보여주기
    public String showEditPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userService.getUserById(userDetails.getId());
        EditForm form = new EditForm();
        form.setBankName(user.getBankName());
        form.setAccountNumber(user.getAccountNumber());
        form.setAccountHolder(user.getAccountHolder());
        model.addAttribute("editForm", form);
        model.addAttribute("exerciseType", ExerciseType.values());
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "edit";
    }

    @PostMapping("/edit") // 내 정보 수정하기
    public String handleEdit(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @ModelAttribute EditForm editForm) {
        userService.updateAccountInfo(userDetails.getId(), editForm);

        // TODO: 성공/실패 메시지 모델
        return "redirect:/user/info";
    }

    @PostMapping("/edit/password") // 내 비밀번호 변경하기
    public String changeUserPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @ModelAttribute ChangePasswordForm changePasswordForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updatePassword(userDetails.getId(), changePasswordForm);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/user/info";
    }






}
