package com.tavarlabs.pos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Category name can only contain letters, numbers, spaces, and hyphens")
    private String name;

    @NotBlank(message = "Stock is required. Type a digit bigger than 1")
    @Pattern(regexp = "^\\d+$", message = "Just type a integer.")
    private int stock;

    @NotBlank(message = "Price is required. Type a digit bigger than 1")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Type a valid number")
    private double price;

}
