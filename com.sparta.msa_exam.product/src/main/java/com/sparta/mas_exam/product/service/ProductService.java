package com.sparta.mas_exam.product.service;

import com.sparta.mas_exam.product.controller.ProductRequest;
import com.sparta.mas_exam.product.domain.Product;
import com.sparta.mas_exam.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @CacheEvict(cacheNames = "productList", allEntries = true)
    public void create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .supplyPrice(request.getSupplyPrice())
                .build();

        productRepository.save(product);
    }

    @Cacheable(cacheNames = "productList")
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
