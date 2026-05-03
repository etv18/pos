package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.product.CreateProductRequest;
import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.dtos.product.UpdateProductRequest;
import com.tavarlabs.pos.entity.Product;
import com.tavarlabs.pos.mappers.ProductMapper;
import com.tavarlabs.pos.repositories.ProductRepository;
import com.tavarlabs.pos.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Random random = new Random();

    @Transactional
    @Override
    public Product createProduct(CreateProductRequest createProductRequest) {
        Product newProduct = new Product();

        newProduct.setName(createProductRequest.getName());
        newProduct.setCode(generateProductCode());
        newProduct.setStock(createProductRequest.getStock());
        newProduct.setPrice(createProductRequest.getPrice());

        return productRepository.save(newProduct);
    }

    @Override
    public Product updateProduct(UpdateProductRequest updateProductRequest) {
        String code = updateProductRequest.getCode().toString();
        Product savedProduct = productRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Product not found. code = " + code));

        if (!savedProduct.getName().equalsIgnoreCase(updateProductRequest.getName()))
            savedProduct.setName(updateProductRequest.getName());

        if(savedProduct.getStock() != updateProductRequest.getStock())
            savedProduct.setStock(updateProductRequest.getStock());

        double epsilon = 0.0001;
        if(Double.compare(savedProduct.getPrice(), updateProductRequest.getPrice()) != epsilon)
            savedProduct.setPrice(updateProductRequest.getPrice());

        return productRepository.save(savedProduct);
    }

    @Override
    public void deleteProduct(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Product not found. code = " + code));
        productRepository.delete(product);
    }

    @Override
    public List<Product> listProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public Product getProduct(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Product not found. code = " + code));
        return product;
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
