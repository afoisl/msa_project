package com.sparta.mas_exam.order.controller;

import com.sparta.mas_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Long> create(@RequestBody List<Long> productIds,
                                       @RequestHeader(value = "X-User-Id", required = true) String userId) {
        Long orderId = orderService.create(productIds, userId);
        return ResponseEntity.ok(orderId);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<Long> addProduct(@PathVariable Long orderId, @RequestBody Long productId) {
        Long id = orderService.addProduct(orderId, productId);
        return ResponseEntity.ok(id);  // 상태 코드와 본문을 함께 설정
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long orderId) {
        OrderResponse response = orderService.get(orderId);
        return ResponseEntity.ok(response);
    }
}
