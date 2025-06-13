package com.zerobase.healthhabit.dto;


import com.zerobase.healthhabit.entity.ExerciseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditForm {

    private String username;
    private ExerciseType exerciseType;

    private String bankName;
    private String accountNumber;
    private String accountHolder;

}
