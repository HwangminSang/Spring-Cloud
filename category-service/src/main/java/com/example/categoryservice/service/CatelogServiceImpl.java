package com.example.categoryservice.service;


import com.example.categoryservice.entity.CatalogEntity;
import com.example.categoryservice.repository.CatelogRepository;
import com.example.categoryservice.vo.RequestCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CatelogServiceImpl implements CatelogService{


    private final CatelogRepository catelogRepository;
    @Override
    public List<CatalogEntity> findAll() {
        return  catelogRepository.findAll();
    }

    @Override
    public Long create(RequestCatalog requestCatalog) {
        CatalogEntity build = CatalogEntity.builder()
                .productId(requestCatalog.getProductId())
                .productName(requestCatalog.getProductName())
                .stock(requestCatalog.getStock())
                .unitPrice(requestCatalog.getUnitPrice())
                .build();
        CatalogEntity save = catelogRepository.save(build);
        return save.getId();
    }
}
