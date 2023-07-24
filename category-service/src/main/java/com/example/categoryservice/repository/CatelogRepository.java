package com.example.categoryservice.repository;

import com.example.categoryservice.entity.CatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatelogRepository extends JpaRepository<CatalogEntity, Long> {

    CatalogEntity findByProductId(String productId);

}
