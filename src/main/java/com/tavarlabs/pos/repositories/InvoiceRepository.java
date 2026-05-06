package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // If duplicated invoices show up at DISTINCT after the SELECT statement
    @Query("SELECT inv FROM Invoice inv LEFT JOIN FETCH inv.lines")
    List<Invoice> findAllWithLines();

    @Query("SELECT inv FROM Invoice inv JOIN FETCH inv.lines WHERE inv.code = :code")
    Optional<Invoice> findByCode(@Param("code") String code);
    boolean existsByCode(String code);
}
