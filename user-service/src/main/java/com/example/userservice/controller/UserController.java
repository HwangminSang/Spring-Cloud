package com.example.userservice.controller;


import com.example.userservice.config.ServiceConfig;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.CreateUserRequestVo;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final ServiceConfig serviceConfig;

    private final Environment environment;
    private final UserService userService;

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String health(){

          return  String.format("It's Working in User Service"
            + ", port(local.server.port)=" + environment.getProperty("local.server.port")
                    + ", port(server.port)=" + environment.getProperty("server.port")
                    + ", token secret=" + environment.getProperty("token.secret"));




    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String welcome(){

        return serviceConfig.getService();
    }



    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody @Validated CreateUserRequestVo createUserRequestVo){



        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(createUserRequestVo, UserDto.class);

        UserDto user = userService.createUser(userDto);


        ResponseUser response = modelMapper.map(user, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> findAllUser(){

        List<UserDto> byAllUser = userService.findByAllUser();


        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ResponseUser> userList = new ArrayList<>();
        byAllUser.forEach(userDto -> {
            userList.add(modelMapper.map(userDto , ResponseUser.class));
        });




        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }
    @GetMapping("/users/{use}/{userId}")
    public ResponseEntity<ResponseUser> findUserInfo(@PathVariable("userId") String userId , @PathVariable("use") String use){

        UserDto userByUserId = userService.getUserByUserId(userId,use);

        ModelMapper modelMapper = new ModelMapper();


        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        ResponseUser user = new ModelMapper().map(userByUserId, ResponseUser.class);


        return ResponseEntity.status(HttpStatus.OK).body(user);

    }
}
