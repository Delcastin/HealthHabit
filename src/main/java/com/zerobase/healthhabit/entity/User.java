package com.zerobase.healthhabit.entity;


import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Entity
public class User { // 사용자 Entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // 사용자 이메일주소

    private String username; // 사용자 이름

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private String preferExercise; // 선호하는 운동 종류

    private LocalDateTime createdAt; // 가입 일시

    private String bankName; // 은행명

    private String accountNumber; // 계좌번호

    private String accountHolder; // 예금주명

}