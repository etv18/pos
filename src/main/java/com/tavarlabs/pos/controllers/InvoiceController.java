package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.entity.Invoice;
import com.tavarlabs.pos.mappers.InvoiceMapper;
import com.tavarlabs.pos.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAllInvoices(){
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<InvoiceDto> invoiceDtos = invoices.stream().map(invoiceMapper::toDto).toList();
        return ResponseEntity.ok(invoiceDtos);
    }
}
