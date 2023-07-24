package com.example.categoryservice.kafka;


import com.example.categoryservice.entity.CatalogEntity;
import com.example.categoryservice.repository.CatelogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KafkaConsumer {


    private final CatelogRepository catelogRepository;



    // 카프카 리스너 ( 해당 토픽에 값이 들어오면 컨슈머를 이용하여 가져온다)
    @KafkaListener(topics = "catalog-topic")
    public void updateQty(String kafkaMessage){
        log.info(String.format("catalog-topic에서 가져온 값을 확인 : %s",kafkaMessage));

        // 역직렬화 필요
        Map<Object , Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try{

            // 받아온 데이터를 key value 의 맵 형태로 바꿔준다.
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {

            });

        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        // 비즈니스 로직

        CatalogEntity catalogEntity = catelogRepository.findByProductId((String) map.get("productId"));


        if(catalogEntity == null){
            log.error("상품 존재 x");
            throw new RuntimeException("해당 카탈로그 존재 x");
        }
        // 수량변경
        catalogEntity.minusStock((Integer) map.get("qty"));



    }


}
