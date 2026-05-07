package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.product.CreateProductRequest;
import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.dtos.product.UpdateProductRequest;
import com.tavarlabs.pos.entity.Product;
import com.tavarlabs.pos.mappers.ProductMapper;
import com.tavarlabs.pos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
@RestController
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request){
        ProductDto productDto = productMapper.toDto(productService.createProduct(request));
        return ResponseEntity.ok(productDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ProductDto> updateProduct(
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ) {
        ProductDto productDto = productMapper.toDto(productService.updateProduct(updateProductRequest));
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping(path = "/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<Product> products = productService.listProducts();
        List<ProductDto> productsDto = products.stream().map(productMapper::toDto).toList();
        return ResponseEntity.ok(productsDto);
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String code){
        Product product = productService.getProduct(code);
        ProductDto productDto = productMapper.toDto(product);
        return ResponseEntity.ok(productDto);
    }
}
