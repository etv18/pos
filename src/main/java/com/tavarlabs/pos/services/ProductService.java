package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.product.CreateProductRequest;
import com.tavarlabs.pos.dtos.product.UpdateProductRequest;
import com.tavarlabs.pos.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest createProductRequest);
    Product updateProduct(String code, UpdateProductRequest updateProductRequest);
    void deleteProduct(String code);
    List<Product> listProducts();
    Product getProduct(String code);
}
