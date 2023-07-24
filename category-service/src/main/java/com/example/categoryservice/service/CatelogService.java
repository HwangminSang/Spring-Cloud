package com.example.categoryservice.service;


import com.example.categoryservice.entity.CatalogEntity;
import com.example.categoryservice.vo.RequestCatalog;

import java.util.List;

public interface CatelogService {

   List<CatalogEntity> findAll();

    Long create(RequestCatalog requestCatalog);
}
