package com.example.userservice.vo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {


  @NotNull(message = "Email cannot be null")
    @Size(min = 2 , message = "Email not be less than two characters")
    @Email
    private String email;


    @NotNull(message = "Password cannot be null")
    @Size(min = 8 , message = "비밀번호 최소 8글자")
    private String password;
}
