package com.tavarlabs.pos.dtos.purchase;

import com.tavarlabs.pos.dtos.purchaseline.PurchaseLineDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseDto {
    private String code;
    private String supplierName;
    private double total;
    private List<PurchaseLineDto> lines;
    private LocalDateTime createdAt;
}
