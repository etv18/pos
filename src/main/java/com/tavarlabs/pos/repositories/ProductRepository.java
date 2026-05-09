package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCode(String code);
    Optional<Product> findByCode(String code);

    @Query("SELECT Count(p) FROM Product p WHERE p.stock < :minimumStock")
    long countProductsWithLowStock(int minimumStock);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock < 1")
    long countProductsOutOfStock();
}
