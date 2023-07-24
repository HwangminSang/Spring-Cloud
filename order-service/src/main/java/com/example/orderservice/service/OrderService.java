package com.example.orderservice.service;



import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;

import java.util.List;

public interface OrderService {


   OrderDto  createOrder(OrderDto orderDetails);



   OrderDto  gerOrderByOrderId(String userId , String orderId);

   Iterable<OrderEntity> gerOrdersByUserId(String userId);



}
