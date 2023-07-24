package com.example.categoryservice.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestCatalog implements Serializable {

    private String productId;

    private String productName;

    private Integer stock;

    private Integer unitPrice;

}
