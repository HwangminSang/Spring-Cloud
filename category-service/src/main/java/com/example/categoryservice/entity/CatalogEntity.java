package com.example.categoryservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "CATALOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "PRODUCT_ID",unique = true ,nullable = false  ,length = 50)
    private String productId;
    @Column(name = "PRODUCT_NAME",length = 50,nullable = false)
    private String productName;

    @Column(name = "STOCK",nullable = false)
    private Integer stock;

    @Column(name = "UNIT_PRICE",nullable = false)
    private Integer unitPrice;


    @Column(name = "CREATED_AT",nullable = false , insertable = false , updatable = false)
    @CreatedDate
    private Date createAt;


    @Builder
    public CatalogEntity(String productId , String productName , int stock , int unitPrice ){
        this.productId =productId;
        this.productName =productName;
        this.stock = stock;
        this.unitPrice = unitPrice;

    }

    public void minusStock(int requestQty){
        stock = this.stock - requestQty;
    }


}
