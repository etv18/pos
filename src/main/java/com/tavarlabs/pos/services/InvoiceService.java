package com.tavarlabs.pos.services;

import com.tavarlabs.pos.entity.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> getAllInvoices();
}
