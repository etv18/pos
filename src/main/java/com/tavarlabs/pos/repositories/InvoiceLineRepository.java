package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {

    @Query("SELECT il FROM InvoiceLine il WHERE il.invoice.code = :code")
    List<InvoiceLine> findInvoiceLinesByInvoiceCode(@Param("code") String code);
}
