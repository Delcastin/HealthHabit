package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_user")
public class User { // 사용자 Entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    private String email; // 사용자 이메일주소

    private String username; // 사용자 이름

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private ExerciseType preferExercise; // 선호하는 운동 종류

    private LocalDateTime createdAt; // 가입 일시

    private String bankName; // 은행명

    private String accountNumber; // 계좌번호

    private String accountHolder; // 예금주명

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // 가입일시 자동생성
    }

}