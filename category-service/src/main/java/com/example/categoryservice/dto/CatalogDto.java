package com.example.categoryservice.dto;


import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;

@Data
public class CatalogDto implements Serializable {


    private String productId;

    private Integer stock;

    private Integer unitPrice;

    private Integer totalPrice;


    private String orderId;

    private String userId;

}
