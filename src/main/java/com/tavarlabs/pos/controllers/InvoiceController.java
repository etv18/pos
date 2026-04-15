package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.invoice.CreateInvoiceRequest;
import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.entity.Invoice;
import com.tavarlabs.pos.mappers.InvoiceMapper;
import com.tavarlabs.pos.services.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(
            @Valid @RequestBody CreateInvoiceRequest createInvoiceRequest
    ){
        Invoice invoice = invoiceService.createInvoice(createInvoiceRequest);
        InvoiceDto invoiceDto = invoiceMapper.toDto(invoice);
        return ResponseEntity.ok(invoiceDto);
    }
}
