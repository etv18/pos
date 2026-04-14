package com.tavarlabs.pos.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String code;
    private String name;
    private int stock;
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
