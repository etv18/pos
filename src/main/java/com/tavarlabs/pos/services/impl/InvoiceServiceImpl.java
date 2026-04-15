package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.invoice.CreateInvoiceRequest;
import com.tavarlabs.pos.dtos.invoiceline.CreateInvoiceLineRequest;
import com.tavarlabs.pos.entity.Invoice;
import com.tavarlabs.pos.entity.InvoiceLine;
import com.tavarlabs.pos.entity.Product;
import com.tavarlabs.pos.repositories.InvoiceLineRepository;
import com.tavarlabs.pos.repositories.InvoiceRepository;
import com.tavarlabs.pos.repositories.ProductRepository;
import com.tavarlabs.pos.services.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceLineRepository invoiceLineRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAllWithLines();
        return invoices;
    }

    @Override
    public Invoice createInvoice(CreateInvoiceRequest invoiceRequest) {
        // Checks
        if(invoiceRequest == null)
            throw new IllegalArgumentException("Invoice request has no content");

        if(invoiceRequest.getLines().isEmpty())
            throw new IllegalArgumentException("This invoice request has lines inside");

        // Starting missing dependencies
        Invoice newInvoice = new Invoice();
        String code = generateInvoiceCode();
        List<InvoiceLine> lines = generateInvoiceLines(invoiceRequest.getLines(), newInvoice);

        // Setting dependencies
        newInvoice.setCode(code);
        newInvoice.setClientName(invoiceRequest.getClientName());
        newInvoice.setLines(lines);

        return invoiceRepository.save(newInvoice);
    }

    private List<InvoiceLine> generateInvoiceLines(List<CreateInvoiceLineRequest> requestedLines, Invoice invoice){
        List<InvoiceLine> lines = new ArrayList<>();
        Product product = null;
        for(CreateInvoiceLineRequest requestedLine : requestedLines){
            product = productRepository.findByCode(requestedLine.getProductCode())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product not found. Product code = "+requestedLine.getProductCode())
                    );
            InvoiceLine invoiceLine = new InvoiceLine();
            invoiceLine.setQuantity(requestedLine.getQuantity());
            invoiceLine.setInvoice(invoice);
            invoiceLine.setProduct(product);
            lines.add(invoiceLine);
        }
        return lines;
    }

    private String generateInvoiceCode() {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long count = invoiceRepository.count();

        String code;
        int attempts = 0;

        do {
            long sequence = count + 1 + attempts;
            code = datePart + String.format("%02d", sequence);
            attempts++;

        } while (invoiceRepository.existsByCode(code) && attempts < 20);

        if (invoiceRepository.existsByCode(code)) {
            throw new RuntimeException("Could not generate unique invoice code after 20 attempts");
        }

        return code;
    }
}
