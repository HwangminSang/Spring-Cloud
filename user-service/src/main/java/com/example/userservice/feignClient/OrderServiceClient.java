package com.example.userservice.feignClient;


import com.example.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")  // 어플리케이션 이름
public interface OrderServiceClient {


    /**
     * 해당 메서드의 반환값 확인필요!
     * @param userId
     * @return
     */

    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
