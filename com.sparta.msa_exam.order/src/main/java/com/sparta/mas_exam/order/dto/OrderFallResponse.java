package com.sparta.mas_exam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFallResponse {
    private Long orderId;
    private List<Long> productIds;
    private String createdBy;
    private String message;
}
