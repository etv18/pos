package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepostory extends JpaRepository<Product, Long> {
}
