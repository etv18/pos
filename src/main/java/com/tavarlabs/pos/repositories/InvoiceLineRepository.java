package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    @Query("SELECT il FROM InvoiceLine il WHERE il.invoice.code = :code")
    List<InvoiceLine> findInvoiceLinesByInvoiceCode(@Param("code") String code);

    @Query("SELECT SUM(il.quantity) FROM InvoiceLine il WHERE il.createdAt BETWEEN :start AND :end")
    Optional<Long> countSoldItemsBasedOnTimeFrame(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
