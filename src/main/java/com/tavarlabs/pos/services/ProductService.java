package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.CreateProductRequest;
import com.tavarlabs.pos.dtos.ProductDto;
import com.tavarlabs.pos.entity.Product;

public interface ProductService {
    ProductDto createProduct(CreateProductRequest createProductRequest);
}
