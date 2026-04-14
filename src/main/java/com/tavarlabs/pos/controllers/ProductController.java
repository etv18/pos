package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.product.CreateProductRequest;
import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.dtos.product.UpdateProductRequest;
import com.tavarlabs.pos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request){
        ProductDto productDto = productService.createProduct(request);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping(path = "/{code}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable String code,
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ) {
        ProductDto productDto = productService.updateProduct(code, updateProductRequest);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping(path = "/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }
}
