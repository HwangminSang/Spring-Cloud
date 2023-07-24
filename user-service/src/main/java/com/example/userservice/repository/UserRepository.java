package com.example.userservice.repository;

import com.example.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository  extends JpaRepository<UserEntity , Long> {


    boolean existsByEmail(String email);




    @Query("SELECT u FROM UserEntity u WHERE u.userId =:userId")
    UserEntity findByUserIdInfo(@Param("userId") String userId);

    UserEntity findByEmail(String username);
}
