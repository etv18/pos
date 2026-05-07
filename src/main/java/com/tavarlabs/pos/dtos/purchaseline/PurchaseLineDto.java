package com.tavarlabs.pos.dtos.purchaseline;

import com.tavarlabs.pos.dtos.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseLineDto {
    private ProductDto product;
    private int quantity;
    private Double total;
}



