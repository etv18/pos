package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.product.CreateProductRequest;
import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.dtos.product.UpdateProductRequest;

public interface ProductService {
    ProductDto createProduct(CreateProductRequest createProductRequest);
    ProductDto updateProduct(String code, UpdateProductRequest updateProductRequest);
    void deleteProduct(String code);
}
