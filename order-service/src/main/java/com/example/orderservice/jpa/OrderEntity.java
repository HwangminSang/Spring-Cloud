package com.example.orderservice.jpa;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "TB_ORDRS")
public class OrderEntity extends  BaseTime implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "PRODUCT_ID",nullable = false  ,length = 50)
    private String productId;
    @Column(name = "QTY",length = 50,nullable = false)
    private Integer qty;
    @Column(name = "UNIT_PRICE",nullable = false)
    private Integer unitPrice;

    @Column(name = "TOTAL_PRICE",nullable = false)
    private Integer totalPrice;

    @Column(name = "USER_ID",nullable = false)
    private String userId;


    @Column(name = "ORDER_ID",unique = true ,nullable = false )
    private String orderId;

}
