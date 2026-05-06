package com.tavarlabs.pos.dtos.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceWithoutLinesDto {
    private String code;
    private String clientName;
    private double total;
    private LocalDateTime createdAt;
}
