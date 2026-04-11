package com.tavarlabs.pos.repositories;

import com.tavarlabs.pos.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
