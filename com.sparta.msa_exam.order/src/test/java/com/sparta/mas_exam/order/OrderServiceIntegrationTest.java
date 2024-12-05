package com.sparta.mas_exam.order;

import com.sparta.mas_exam.order.domain.Order;
import com.sparta.mas_exam.order.domain.OrderProduct;
import com.sparta.mas_exam.order.repository.OrderRepository;
import com.sparta.mas_exam.order.client.ProductClient;
import com.sparta.mas_exam.order.dto.OrderAddRequest;
import com.sparta.mas_exam.order.dto.OrderResponse;
import com.sparta.mas_exam.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // "test" 프로파일 활성화 (application-test.yml을 사용)
@Transactional  // 각 테스트 메서드 끝날 때마다 롤백하여 DB 상태를 초기화
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    private String userId;

    @BeforeEach
    public void setup() {
        // 테스트 전 준비 작업
        userId = "test-user";
    }

    @Test
    public void testCreateOrder() {
        // 상품 ID 목록 준비 (상품이 실제로 존재하는지 확인)
        List<Long> productIds = List.of(1L, 2L, 3L);  // 예시로 1, 2, 3번 상품 사용

        // Order 생성
        Long orderId = orderService.create(productIds, userId);

        // 생성된 주문이 DB에 존재하는지 확인
        Order order = orderRepository.findById(orderId).orElseThrow();

        assertThat(order).isNotNull();
        assertThat(order.getProductIds()).hasSize(3);
        assertThat(order.getCreatedBy()).isEqualTo(userId);
    }

    @Test
    public void testAddProductToOrder() {
        // 기존 주문을 먼저 생성
        List<Long> productIds = List.of(1L, 2L);
        Long orderId = orderService.create(productIds, userId);

        // 추가할 상품 정보
        OrderAddRequest addRequest = new OrderAddRequest(3L);

        // 상품 추가
        Long updatedOrderId = orderService.addProduct(orderId, addRequest);

        // 추가된 상품이 주문에 반영되었는지 확인
        Order updatedOrder = orderRepository.findById(updatedOrderId).orElseThrow();

        assertThat(updatedOrder.getProductIds()).hasSize(3);
    }

    @Test
    public void testGetOrder() {
        // 상품 ID 목록 준비
        List<Long> productIds = List.of(1L, 2L, 3L);
        Long orderId = orderService.create(productIds, userId);

        // 주문 조회
        OrderResponse orderResponse = orderService.get(orderId);

        // 응답 데이터 검증
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderId()).isEqualTo(orderId);
        assertThat(orderResponse.getProductIds()).hasSize(3);
    }
}
