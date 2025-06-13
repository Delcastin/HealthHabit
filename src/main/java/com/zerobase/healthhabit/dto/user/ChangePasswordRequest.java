package com.zerobase.healthhabit.dto.user;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
}
