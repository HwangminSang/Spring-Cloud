package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final UserService userService;

    private final Environment environment;



    //인증시
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {

        try {
            // post 방식은 inputSteam
            RequestLogin requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 인증 처리 매니저
               return getAuthenticationManager().authenticate(
                       new UsernamePasswordAuthenticationToken(requestLogin.getEmail()
                               , requestLogin.getPassword()
                               , new ArrayList<>())
               );



        }catch (IOException e){

            throw  new RuntimeException(e.getMessage());
        }
    }


    

    // 인증 성공시 처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

            log.debug(((User)authResult.getPrincipal()).getUsername());

            String userName = (((User)authResult.getPrincipal()).getUsername());

            UserDto userDetails = userService.getUserDetailsByEmail(userName);


            // 토큰 생성후 응답


         String token = Jwts.builder()
                 .setSubject(userDetails.getUserId())
                 .setExpiration(new Date(System.currentTimeMillis() + Long.valueOf(environment.getProperty("token.expiration_time"))))
                 .signWith(SignatureAlgorithm.HS512 , environment.getProperty("token.secret"))
                 .compact();

        System.out.println("token.secret = " + environment.getProperty("token.secret"));


        response.addHeader("token" , token);
        response.addHeader("userId" , userDetails.getUserId());



    }

}
