package com.example.orderservice.dto.kafka;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    // 해당 디비의 칼럼 이름


    private String order_id;

    private String user_id;

    private String product_id;

    private int qty;

    private int unit_price;

    private int total_price;
}
