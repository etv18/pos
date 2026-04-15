package com.tavarlabs.pos.dtos.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "Product name is required") // Annotations just for integers
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Product name can only contain letters, numbers, spaces, and hyphens")
    private String name;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be 0 or greater")
    private Integer stock;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price is required. Type a digit bigger than 0")
    private Double price;

}
