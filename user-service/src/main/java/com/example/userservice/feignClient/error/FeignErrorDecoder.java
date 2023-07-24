package com.example.userservice.feignClient.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


// fegin 클라이언트 이용시 ErrorDecoder  에러 처리
@Slf4j
@Component  // 빈으로 등록
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {


    private final Environment environment;



    @Override
    public Exception decode(String methodKey, Response response) {
            log.info(methodKey);

        switch (response.status()){
            case 400 :
                if(methodKey.contains("getOrders")){
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status())
                            ,"Bad-Request.");
                }
                break;
            case 404:
                if(methodKey.contains("getOrders")){
                    // responseStatus를 404로 !! 그전에는 500
                    return new ResponseStatusException(HttpStatusCode.valueOf(response.status())
                    ,environment.getProperty("order_service.exception.order_is_empty"));
                }
                break;
            default:
                return new Exception(response.reason());
        }

        return null;

    }
}
