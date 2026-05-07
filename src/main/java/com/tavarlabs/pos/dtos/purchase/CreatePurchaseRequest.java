package com.tavarlabs.pos.dtos.purchase;

import com.tavarlabs.pos.dtos.purchaseline.CreatePurchaseLineRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseRequest {

    @NotBlank(message = "Client name is required")
    @Pattern(regexp = "^[\\w\\s-]+$", message = "Client name can only contain letters, numbers, spaces, and hyphens")
    private String supplierName;

    @NotNull(message = "Purchase cannot be created with no products")
    @Size(message = "Purchase must have at least one item")
    private List<CreatePurchaseLineRequest> lines;
}
