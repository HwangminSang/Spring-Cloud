package com.example.apiservice.config;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * 모든 라우터에 적용할수있는 공통 필터
 */
@Component
@Slf4j
// yml 파일에 등록한다.
public class GolobalFilter extends AbstractGatewayFilterFactory<GolobalFilter.Config> {

    public GolobalFilter(){
        super(Config.class);
    }

    /**
     * 설정 정보를 넣어준다.
     * yml 파일에서 받아올수있다
     * 롬복에서 기본적으로 boolean 타입은 is를 접두사로 붙여 getter, setter를 만들어준다.
     */
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    // request ,response 객체 전처리 후처리가 가능하다 ( 필터역할 )
    @Override
    public GatewayFilter apply(Config config) {


        // 해당 객체는 GatewayFilter를 반환
        // 필터에 들어와서 전처리
        return ((exchange, chain) -> {
            // 기존 동기식 tomcat을 사용하는것이 아니라 비동기식 netty사용 하기때문에 ServerHttpRequest
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("BaseMessage를 확인합니다 : {}",config.getBaseMessage());

            // 로그를 찍을지 yml에 설정값을 따른다
            if(config.isPreLogger()){

                log.info(" GolbalFilter :  로그:  {} Request path를 확인합니다 : {}",config.isPreLogger(),request.getPath());

            }
            // 다음 필터에게 넘기고
            return chain.filter(exchange)
                    // 모든 요청을 처리 마지막 클라이언트에게 응답할때 후처리
                    .then(Mono.fromRunnable(()->{
                        if(config.postLogger){
                            log.info("GolbalFilter : 모든 요청이 끝난후 포스터 로그 : {}  설정으로 statusCode 확인 후처리 : {}",config.isPostLogger(),response.getStatusCode());
                        }
                    }));

        });
    }




}
