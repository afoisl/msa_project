package com.sparta.mas_exam.product.controller;

import com.sparta.mas_exam.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        productService.create(request);
        return ResponseEntity.ok().build();
    }

}
