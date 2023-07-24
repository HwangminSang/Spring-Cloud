package com.example.apiservice.config;


import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private  Environment environment;

    public AuthorizationHeaderFilter(Environment environment){
        super(Config.class);
        this.environment =environment;
    }


    public static class Config{

    }


    @Override
    public GatewayFilter apply(Config config) {

        // 해당 객체는 GatewayFilter를 반환
        // 필터에 들어와서 전처리
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 헤더 여부 확인
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))  return onError(exchange , "NO AUTHORIZATION HEARDER" , HttpStatus.UNAUTHORIZED);



            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer" , "");

            if(!isJwtValid(jwt)) return onError(exchange , "JWT token is not valid" , HttpStatus.UNAUTHORIZED);

            // 다음 필터에게 넘기고
            return chain.filter(exchange);


        });
    }
    //토큰 유효성 검증
    private boolean isJwtValid(String jwt) {

      boolean returnValue = true;

      // user-service에서 subject에 userId를 넣어서 만들었다.
      String subject = null;
        System.out.println("token.secret = " + environment.getProperty("token.secret"));

      try {
          subject = Jwts.parser().setSigningKey(environment.getProperty("token.secret"))
                  .parseClaimsJws(jwt).getBody()
                  .getSubject();
      }catch (Exception e){

        return false;
      }

      if(StringUtils.isEmpty(subject)) return false;



      return returnValue;

    }


    // webFlux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}



