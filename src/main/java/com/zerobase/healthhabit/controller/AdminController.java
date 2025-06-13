package com.zerobase.healthhabit.controller;


import com.zerobase.healthhabit.entity.User;
import com.zerobase.healthhabit.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/pending")
    public String showPendingUsers(Model model) {
        List<User> pendingUsers = userService.getPendingUsers();
        model.addAttribute("users", pendingUsers);
        return "admin/pending";
    }

    @PostMapping("/approve/{userId}")
    public String approveUser(@PathVariable Long userId) {
        User approvedUser = userService.approveUser(userId);
        return "redirect:/admin/pending";
    }
}
