package com.tavarlabs.pos.dtos.purchaseline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseLineRequest {
    @NotNull(message = "You need to provide a valid product code for adding it to the purchase")
    @NotBlank(message = "You need to provide a valid product code for adding it to the purchase")
    private String productCode;

    @NotNull(message = "You must need to add the quantity of the product")
    private Integer quantity;
}
