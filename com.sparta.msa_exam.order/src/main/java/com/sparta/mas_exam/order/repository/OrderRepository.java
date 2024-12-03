package com.sparta.mas_exam.order.repository;

import com.sparta.mas_exam.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
