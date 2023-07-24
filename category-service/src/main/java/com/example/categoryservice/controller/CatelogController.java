package com.example.categoryservice.controller;


import com.example.categoryservice.entity.CatalogEntity;
import com.example.categoryservice.service.CatelogService;
import com.example.categoryservice.vo.RequestCatalog;
import com.example.categoryservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog-service/")
@RequiredArgsConstructor
@Slf4j
public class CatelogController {

    private final CatelogService catelogService;


    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> findAllUser(){
        System.out.println("\"\" = " + "확인좀하자");
        List<CatalogEntity> all = catelogService.findAll();


        ModelMapper modelMapper = new ModelMapper();
        // e
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<ResponseCatalog> userList = new ArrayList<>();
        all.forEach(catalogEntity -> {
            userList.add(modelMapper.map(catalogEntity , ResponseCatalog.class));
        });




        return ResponseEntity.status(HttpStatus.OK).body(userList);

    }


    @PostMapping("/catalogs")
    public ResponseEntity<Map<String , Object>> createCatalog(@RequestBody RequestCatalog requestCatalog){


        Long id = catelogService.create(requestCatalog);


        if(id == null){
            log.error("생성실패");
            throw  new RuntimeException("생성실패");

        }

       Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("id", id);
        responseMap.put("product-id", requestCatalog.getProductId());



        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);

    }
}
