package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.CreateProductRequest;
import com.tavarlabs.pos.dtos.ProductDto;
import com.tavarlabs.pos.entity.Product;
import com.tavarlabs.pos.mappers.ProductMapper;
import com.tavarlabs.pos.repositories.ProductRepository;
import com.tavarlabs.pos.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Random random = new Random();

    @Transactional
    @Override
    public ProductDto createProduct(CreateProductRequest createProductRequest) {
        Product newProduct = new Product();

        newProduct.setName(createProductRequest.getName());
        newProduct.setCode(generateProductCode());
        newProduct.setStock(createProductRequest.getStock());
        newProduct.setPrice(createProductRequest.getPrice());

        return productMapper.toDto(productRepository.save(newProduct));
    }

    public String generateProductCode() {
        String code;
        int attempts = 0;

        do {
            code = String.format("%04d", random.nextInt(10000)); // 0000 - 9999
            attempts++;

            if (attempts > 20) {
                throw new RuntimeException("Unable to generate product product code");
            }

        } while (productRepository.existsByCode(code));

        return code;
    }
}
