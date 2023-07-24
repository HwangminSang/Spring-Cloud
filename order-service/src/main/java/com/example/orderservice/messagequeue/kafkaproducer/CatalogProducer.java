package com.example.orderservice.messagequeue.kafkaproducer;


import com.example.orderservice.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogProducer {


    private final KafkaTemplate<String , String> kafkaTemplate;

    private static final String CATALOG_TOPIC = "catalog-topic";


    // JSON 형태의 문자열로 TOPIC으로 보낸다.

    public OrderDto sendCatalogService(OrderDto orderDto){

        ObjectMapper objectMapper = new ObjectMapper();
        String jsoninString = "";

        try{
            // dto를 json 형태의 문자열로 변경
            jsoninString = objectMapper.writeValueAsString(orderDto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 해당 토픽으로 전송
        kafkaTemplate.send(CATALOG_TOPIC , jsoninString);

        log.info(String.format("카프카 프로듀서에서 해당 %s 토픽으로 데이터 : %s를 보냈다","catalog-topic",jsoninString));


        return orderDto;
    }


}
