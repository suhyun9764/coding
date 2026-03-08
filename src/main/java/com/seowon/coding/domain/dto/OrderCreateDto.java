package com.seowon.coding.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderCreateDto {
    private String customerName;
    private String customerEmail;
    private List<Long> productIds;
    private List<Integer> quantities;
}
