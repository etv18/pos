package com.tavarlabs.pos.dtos.invoiceline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInvoiceLineRequest {
    private String productCode;
    private int quantity;
}
