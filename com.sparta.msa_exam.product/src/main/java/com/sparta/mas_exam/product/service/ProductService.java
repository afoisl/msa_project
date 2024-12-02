package com.sparta.mas_exam.product.service;

import com.sparta.mas_exam.product.controller.ProductRequest;
import com.sparta.mas_exam.product.domain.Product;
import com.sparta.mas_exam.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .supplyPrice(request.getSupplyPrice())
                .build();

        productRepository.save(product);
    }
}
