package com.zerobase.healthhabit.exception;

public class UserInfoDoesNotMatchException extends RuntimeException {

    public UserInfoDoesNotMatchException() {
        super("사용자 정보가 일치하지 않습니다!");
    }
}
