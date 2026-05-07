package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query("SELECT p FROM Purchase p JOIN FETCH p.lines")
    List<Purchase> findAllWithLines();

    @Query("SELECT p FROM Purchase p JOIN FETCH p.lines WHERE p.code = :code")
    Optional<Purchase> findByCode(@Param("code") String code);

    boolean existsByCode(String code);
}
