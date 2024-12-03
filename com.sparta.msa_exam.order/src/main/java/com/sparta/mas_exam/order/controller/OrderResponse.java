package com.sparta.mas_exam.order.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private List<Long> productIds;
}
