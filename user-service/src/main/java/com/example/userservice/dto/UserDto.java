package com.example.userservice.dto;


import com.example.userservice.vo.ResponseOrder;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {


    private String email;
    private String name;

    private String userId;

    private String password;

    private Date CreateDate;

    private String encryptedPwd;


    private List<ResponseOrder> orderList;

}
