package com.example.orderservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    OrderEntity findByUserIdAndOrderId(String userId,String orderId);


    Iterable<OrderEntity> findByUserId(String userId);

}
