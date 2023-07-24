package com.example.orderservice.messagequeue.kafkaproducer;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.kafka.Field;
import com.example.orderservice.dto.kafka.KafkaOrderDto;
import com.example.orderservice.dto.kafka.Payload;
import com.example.orderservice.dto.kafka.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String , String> kafkaTemplate;

    private static final String ORDER_TOPIC = "TB_ORDRS";  // 커넥터의 topics와 같아야하고 테이블에도 존재해야함


    List<Field> fieldList = Arrays.asList(new Field("string" , true , "order_id"),
            new Field("string" , true , "user_id"),
            new Field("string" , true , "product_id"),
            new Field("int32" , true , "qty"),
            new Field("int32" , true , "unit_price"),
            new Field("int32" , true , "total_price"));

    Schema schema  = Schema.builder()
            .type("struct")
            .fields(fieldList)
            .optional(false)
            .name("TB_ORDRS123213")  // 이건 어떤 이름을 해도 상관없다
            .build();

    // JSON 형태의 문자열로 TOPIC으로 보낸다.

    public OrderDto sendConnector(OrderDto orderDto){

        // 커넥터로 보낼 payload 생성
        Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .total_price(orderDto.getTotalPrice())
                .unit_price(orderDto.getUnitPrice())
                .user_id(orderDto.getUserId())
                .build();

        KafkaOrderDto kafkaOrderDto = KafkaOrderDto.builder()
                .schema(schema)
                .payload(payload)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsoninString = "";

        try{
            // dto를 json 형태의 문자열로 변경
            jsoninString = objectMapper.writeValueAsString(kafkaOrderDto);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 해당 토픽으로 전송
        kafkaTemplate.send(ORDER_TOPIC , jsoninString);

        log.info(String.format("카프카 프로듀서에서 해당 %s 토픽으로 데이터 : %s를 보냈다",ORDER_TOPIC,kafkaOrderDto));


        return orderDto;
    }


}
