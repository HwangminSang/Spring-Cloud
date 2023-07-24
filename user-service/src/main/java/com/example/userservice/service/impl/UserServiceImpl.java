package com.example.userservice.service.impl;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.feignClient.OrderServiceClient;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import feign.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    private final Environment environment;

    private final RestTemplate restTemplate;


    private final OrderServiceClient orderServiceClient;


    // 커스텀한게 적용된다
    private final CircuitBreakerFactory circuitBreakerFactory;



    @Override
    public UserDto createUser(UserDto userDto) {

        ModelMapper modelMapper = new ModelMapper();
        // 컬럼명 하나하나 다 비교!
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userDto.setUserId(UUID.randomUUID().toString());

        boolean b = userRepository.existsByEmail(userDto.getEmail());
        if(b){

            throw new RuntimeException("이미존재하는 회원");
        }
        UserEntity userEntity = modelMapper.map(userDto , UserEntity.class);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userRepository.save(userEntity);


        return userDto;
    }




    @Override
    public List<UserDto> findByAllUser() {
        ModelMapper modelMapper = new ModelMapper();

        List<UserEntity> allUser = userRepository.findAll();

        List<UserDto> userDtoList = new ArrayList<>();
        allUser.forEach(user ->{
            UserDto map = modelMapper.map(user, UserDto.class);
            userDtoList.add(map);
        });
        return userDtoList;
    }

    @Override
    public UserDto getUserDetailsByEmail(String userName) {

        UserEntity byEmail = userRepository.findByEmail(userName);

        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(byEmail, UserDto.class);

        return userDto;
    }



    /**
     * 유저 정보를 가져온다.
     * 레스트템플릿 , 페인클라이언트   , 카프카 이용 다른 서비스와 통신
     * @param userId
     * @return
     */
    @Override
    public UserDto getUserByUserId(String userId,String use) {
        ModelMapper modelMapper = new ModelMapper();
        // 컬럼명 하나하나 다 비교!
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity findedUser = userRepository.findByUserIdInfo(userId);

        if(findedUser == null){
            throw new RuntimeException("존재하지않는유저");
        }
        UserDto userDto = modelMapper.map(findedUser, UserDto.class);

        /**
         *  use에 따라 restTemplete , kafka 등 사용방법이 다르게 적요
         */

        List<ResponseOrder> responseOrderList = new ArrayList<>();

        userDto.setOrderList(responseOrderList);

        if (use.equals("restTemplate")){

            String orderUrl  = String.format(environment.getProperty("order_service.url") , userId);

            // 외부에 호출
            ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<ResponseOrder>>() {});

            responseOrderList  = orderListResponse.getBody();
            userDto.setOrderList(responseOrderList);

            /* Using a feignCLient ,  */

        } else if (use.equals("feignClient")) {

            // feignClient의  ErrorDecoder 사용해여 에러 처리한다.
            //List<ResponseOrder> orders = orderServiceClient.getOrders(userId);

            // 서킷 브레이커 사용 ( 레슬리안 4j ) 다른 서비스와 통신중 문제 있을때 해결
            /**
             *  1. orderServiceClient.getOrders(userId) order 마이크로 서비스 호출
             *  2. 문제가 있는경우 빈 배열 반환.
             *  3. <T> T run(Supplier<T> toRun, Function<Throwable, T> fallback);
             */

            log.info("order 서비스 호출전");
            CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
            List<ResponseOrder> orders = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                    throwable -> new ArrayList<>());
            userDto.setOrderList(orders);
            log.info("order 서비스 호출후");

        }

        return userDto;
    }


    // userDetailServer 인증처리
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null){
            throw  new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail()  , userEntity.getPassword() ,
                true , true  , true, true, new ArrayList<>());

    }






}
