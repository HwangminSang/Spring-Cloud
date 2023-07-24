package com.example.orderservice.service;


import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{



    private final OrderRepository orderRepository;


    @Override
    public OrderDto createOrder(OrderDto orderDetails) {

        ModelMapper modelMapper = new ModelMapper();
        // 컬럼명 하나하나 다 비교!
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        orderDetails.setOrderId(UUID.randomUUID().toString());
        // 상품 합계 가격 - >  수량 * 단가
        orderDetails.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());
        OrderEntity orderEntity = modelMapper.map(orderDetails , OrderEntity.class);

        orderRepository.save(orderEntity);


        return modelMapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public OrderDto gerOrderByOrderId(String userId , String orderId) {

        OrderEntity byOrderId = orderRepository.findByUserIdAndOrderId(userId,orderId);


        return new ModelMapper().map(byOrderId , OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> gerOrdersByUserId(String userId) {

        // 해당 유저의 주문 내역
        return orderRepository.findByUserId(userId);
    }
}
