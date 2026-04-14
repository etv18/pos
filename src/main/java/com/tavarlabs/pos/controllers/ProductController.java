package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.CreateProductRequest;
import com.tavarlabs.pos.dtos.ProductDto;
import com.tavarlabs.pos.mappers.ProductMapper;
import com.tavarlabs.pos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createCategory(@Valid @RequestBody CreateProductRequest request){
        ProductDto productDto = productService.createProduct(request);
        return ResponseEntity.ok(productDto);
    }
}
