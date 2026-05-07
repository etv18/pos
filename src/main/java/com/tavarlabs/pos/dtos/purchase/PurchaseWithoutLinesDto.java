package com.tavarlabs.pos.dtos.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseWithoutLinesDto {
    private String code;
    private String supplierName;
    private double total;
    private LocalDateTime createdAt;
}
