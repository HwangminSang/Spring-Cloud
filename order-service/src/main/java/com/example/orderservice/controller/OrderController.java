package com.example.orderservice.controller;


import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.messagequeue.kafkaproducer.CatalogProducer;
import com.example.orderservice.messagequeue.kafkaproducer.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final CatalogProducer catalogProducer;

    private final OrderProducer orderProducer;

    // 주문생성
    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createUser(@PathVariable("userId") String userId , @RequestBody @Validated RequestOrder requestOrder){

        log.info("개인의 주문정보 생성 시작");

        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);

        orderDto.setUserId(userId);

        /**
         * jpa를 통해 직접적으로 db에 데이터 처리
         */
        OrderDto order = orderService.createOrder(orderDto);
        ResponseOrder response = modelMapper.map(order, ResponseOrder.class);



        /**
         * 카프카를 통해서  어플리케이션 단에서 db와 연동하여 데이터를 처리하지않고 단순히 mq 에 넣어둔 후 어플리케이션은 다른 작업을 진행할수있다.
         * lock을 피해갈수도 있다.
         * 1. 주문생성 아이디
         * 2. 총가격
         * 3.응답객체 생성
         * 4. 카탈로그 서비스에 재고 수량 변경
         * 5.
         */

        //주문 아이디 생성
//        orderDto.setOrderId(UUID.randomUUID().toString());
////        // 상품 합계 가격 - >  수량 * 단가
//        orderDto.setTotalPrice(requestOrder.getQty() * requestOrder.getUnitPrice());
//
//        /* 주문을 생성한후 해당 값을 카탈로그 서비스로 전송  procuer - topic -consumer */
        catalogProducer.sendCatalogService(orderDto);
//        /* 씽크 connect -> topic -> 소스 connect 이용 */
//        /* 카프카 커넥터 기동 필요 */
//        orderProducer.sendConnector(orderDto);
////
////
//        ResponseOrder response = modelMapper.map(orderDto, ResponseOrder.class);

        log.info("개인의 주문정보 생성 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> findOrders(@PathVariable("userId") String userId){

        log.info("개인의 주문정보 조회 시작");
        Iterable<OrderEntity> orderEntities = orderService.gerOrdersByUserId(userId);


        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ResponseOrder> orderList = new ArrayList<>();
        orderEntities.forEach(order -> {
            orderList.add(modelMapper.map(order , ResponseOrder.class));
        });



        log.info("개인의 주문정보 조회 응답");
        return ResponseEntity.status(HttpStatus.OK).body(orderList);

    }
    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<ResponseOrder> findAllUser(@PathVariable("userId") String userId,@PathVariable("orderId") String orderId){

        OrderDto orderDto = orderService.gerOrderByOrderId(userId , orderId);

        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);


        ResponseOrder order = new ModelMapper().map(orderDto, ResponseOrder.class);


        return ResponseEntity.status(HttpStatus.OK).body(order);

    }


}
