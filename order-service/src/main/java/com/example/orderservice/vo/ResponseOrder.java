package com.example.orderservice.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 반환 x
public class ResponseOrder {

    private String productId;

    private Integer qty;

    private Integer totalPrice;

    private Integer unitPrice;

    private Date createAt;

    private String orderId;
}
