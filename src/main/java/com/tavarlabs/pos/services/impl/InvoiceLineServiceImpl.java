package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.invoiceline.CreateInvoiceLineRequest;
import com.tavarlabs.pos.entity.InvoiceLine;
import com.tavarlabs.pos.services.InvoiceLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InvoiceLineServiceImpl implements InvoiceLineService {
    @Override
    public List<InvoiceLine> getLinesFromAnInvoice(String invoiceCode) {
        return List.of();
    }

    @Override
    public List<InvoiceLine> createInvoiceLines(List<CreateInvoiceLineRequest> lines) {
        return List.of();
    }
}
