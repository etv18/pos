package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepostory extends JpaRepository<Product, Long> {
}
