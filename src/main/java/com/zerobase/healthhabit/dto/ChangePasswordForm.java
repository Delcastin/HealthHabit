package com.zerobase.healthhabit.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {


    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
