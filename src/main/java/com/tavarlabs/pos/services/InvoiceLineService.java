package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.invoiceline.CreateInvoiceLineRequest;
import com.tavarlabs.pos.entity.InvoiceLine;

import java.util.List;

public interface InvoiceLineService {
    List<InvoiceLine> getLinesFromAnInvoice(String invoiceCode);
    List<InvoiceLine> createInvoiceLines(List<CreateInvoiceLineRequest> lines);
}
