package com.sparta.mas_exam.product.domain;

import com.sparta.mas_exam.product.controller.ProductResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int supplyPrice;
    private String description;
    private int quantity;

    public ProductResponse toResponse() {
        return new ProductResponse(
                this.id,
                this.name,
                this.description,
                this.supplyPrice,
                this.quantity
        );
    }

    public void reduceQuantity(int i) {
        this.quantity = this.quantity - i;
    }
}
