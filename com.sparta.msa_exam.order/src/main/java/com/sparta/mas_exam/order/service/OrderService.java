package com.sparta.mas_exam.order.service;

import com.sparta.mas_exam.order.client.ProductClient;
import com.sparta.mas_exam.order.domain.Order;
import com.sparta.mas_exam.order.domain.OrderProduct;
import com.sparta.mas_exam.order.dto.OrderAddRequest;
import com.sparta.mas_exam.order.dto.OrderFallResponse;
import com.sparta.mas_exam.order.dto.OrderResponse;
import com.sparta.mas_exam.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreate")
    public Long create(List<Long> productIds, String userId) {
        List<OrderProduct> validProductIds = productIds.stream()
                .filter(this::isValidProduct)// 유효한 상품만 필터링
                .map(productId -> OrderProduct.builder()
                        .productId(productId) // OrderProduct 객체를 생성
                        .build())
                .toList();

        Order order = Order.builder()
                .productIds(validProductIds)
                .createdBy(userId) // createdBy 필드에 userId 설정
                .build();
        return orderRepository.save(order).getId();
    }

    public OrderFallResponse fallbackCreate(List<Long> productIds, String userId, Throwable t) {
        log.error("Fallback triggered for creating order. Error: {}", t.getMessage());

        String fallbackMessage = "잠시 후에 주문 추가를 요청 해주세요.";
        // 대체 데이터
        return new OrderFallResponse(null, productIds, userId, fallbackMessage);
    }

    public Long addProduct(Long orderId, OrderAddRequest add) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (!isValidProduct(add.productId())) {
            throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        }

        order.addProduct(OrderProduct.builder().productId(add.productId()).build());

        return orderRepository.save(order).getId();
    }

    @Cacheable(cacheNames = "orderCache", key = "args[0]")
    public OrderResponse get(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        return OrderResponse.builder()
                .orderId(order.getId())
                .productIds(order.getProductIds())
                .build();
    }

    // 상품 존재 여부 확인
    private boolean isValidProduct(Long productId) {
        return productClient.getProduct(productId) != null;
    }


}
