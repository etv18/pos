package com.tavarlabs.pos.dtos.invoice;

import com.tavarlabs.pos.dtos.invoiceline.InvoiceLineDto;
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
public class InvoiceDto {
    private String code;
    private String clientName;
    private double total;
    private List<InvoiceLineDto> lines;
    private LocalDateTime createdAt;
}
