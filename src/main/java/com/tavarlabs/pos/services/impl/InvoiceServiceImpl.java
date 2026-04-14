package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.entity.Invoice;
import com.tavarlabs.pos.repositories.InvoiceRepository;
import com.tavarlabs.pos.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAllWithLines();
        return invoices;
    }
}
