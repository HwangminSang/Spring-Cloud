package com.example.userservice.vo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestVo {


    @NotBlank(message = "이메일을 입력하셔야합니다")
    @Size(min = 2 , message = "최소 2글자 이상을 입력하셔야합니다")
    @Email(message = "email 형식을 지켜주세요")
    private String email;



    @NotBlank(message = "비밀번호를 입력하셔야합니다")
    @Size(min = 2 , message = "최소 2글자 이상을 입력하셔야합니다")
    private String password;



    @NotBlank(message = "이름 입력하셔야합니다")
    @Size(min = 2 , message = "최소 2글자 이상을 입력하셔야합니다")
    private String name;

}
