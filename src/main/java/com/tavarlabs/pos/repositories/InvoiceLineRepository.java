package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {
}
