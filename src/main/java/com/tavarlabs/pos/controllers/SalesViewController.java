package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.dtos.invoice.InvoiceWithoutLinesDto;
import com.tavarlabs.pos.mappers.InvoiceMapper;
import com.tavarlabs.pos.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/sales")
public class SalesViewController {
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    @GetMapping(path = "/invoices")
    public String viewAllInvoices(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<InvoiceWithoutLinesDto> invoiceWithoutLines = invoiceService.getAllInvoices().stream()
                        .map(invoiceMapper::toInvoiceWithoutLinesDto)
                        .toList();

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("invoices", invoiceWithoutLines);
        model.addAttribute("tabTitle", "Sales");

        return "sales/index";
    }

    @GetMapping(path = "/invoices/{invoiceCode}")
    public String viewInvoiceDetails(
            Model model,
            Authentication authentication,
            @PathVariable("invoiceCode") String code
    ){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        InvoiceDto invoiceDto = invoiceMapper.toDto(invoiceService.findInvoiceByCode(code));

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("tabTitle", "Sales");

        return "sales/invoiceDetails";
    }
}
