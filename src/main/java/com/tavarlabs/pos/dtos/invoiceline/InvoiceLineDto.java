package com.tavarlabs.pos.dtos.invoiceline;

import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.dtos.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceLineDto {
    private ProductDto product;
    private int quantity;
    private double total;
}



