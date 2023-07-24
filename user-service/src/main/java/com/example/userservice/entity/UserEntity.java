package com.example.userservice.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_USER")
@Data
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


   @Column(unique = true ,nullable = false  ,length = 50 ,name = "EMAIL")
   private String email;
    @Column(length = 50 , name = "NAME")
   private String name;

    @Column(unique = true ,nullable = false,name = "USER_ID")
    private String userId;

    @Column(unique = true ,nullable = false , name = "PASSWORD")
   private String password;


}
