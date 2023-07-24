package com.example.orderservice.messagequeue;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String , String> producerFactory(){

        // 설정
        Map<String , Object> properties = new HashMap<>();

//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , "127.0.0.1:9092"); // 로컬전용
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG , "172.18.0.101:9092");  //컨테이너

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG , StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG , StringSerializer.class);



        return new DefaultKafkaProducerFactory<>(properties);
    }


    // 토픽으로 데이터를 보낸다
    @Bean
    public KafkaTemplate<String , String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }


}
