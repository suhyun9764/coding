package com.seowon.coding.service;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {
    private Long productId;

    @Min(value = 1, message = "수량은 1개 이상부터 가능합니다")
    private Integer quantity;
}