package com.seowon.coding.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "사용자 이름이 누락되었습니다.")
    private String customerName;
    @NotNull(message = "사용자 이메일이 누락되었습니다.")
    private String customerEmail;

    @NotNull(message = "제품 정보가 누락되었습니다.")
    @NotEmpty
    private List<Long> productIds;

    @NotNull(message = "수량이 누락되었습니다.")
    @NotEmpty
    private List<Integer> quantities;
}
