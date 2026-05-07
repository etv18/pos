package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.invoice.InvoiceDto;
import com.tavarlabs.pos.dtos.invoice.InvoiceWithoutLinesDto;
import com.tavarlabs.pos.dtos.purchase.PurchaseDto;
import com.tavarlabs.pos.dtos.purchase.PurchaseWithoutLinesDto;
import com.tavarlabs.pos.mappers.InvoiceMapper;
import com.tavarlabs.pos.mappers.PurchaseMapper;
import com.tavarlabs.pos.services.InvoiceService;
import com.tavarlabs.pos.services.PurchaseService;
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
@RequestMapping(path = "/history")
public class HistoryViewController {
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;

    @GetMapping(path = "/invoices")
    public String viewAllInvoices(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<InvoiceWithoutLinesDto> invoiceWithoutLines = invoiceService.getAllInvoices().stream()
                        .map(invoiceMapper::toInvoiceWithoutLinesDto)
                        .toList();

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("invoices", invoiceWithoutLines);
        model.addAttribute("tabTitle", "Sales");

        return "history/invoices";
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

        return "history/invoiceDetails";
    }

    @GetMapping(path = "/purchases")
    public String viewAllPurchases(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<PurchaseWithoutLinesDto> purchaseWithoutLines = purchaseService.getAllPurchases().stream()
                .map(purchaseMapper::toPurchaseWithoutLinesDto)
                .toList();

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("purchases", purchaseWithoutLines);
        model.addAttribute("tabTitle", "Sales");

        return "history/purchases";
    }

    @GetMapping(path = "/purchases/{purchaseCode}")
    public String viewPurchaseDetails(
            Model model,
            Authentication authentication,
            @PathVariable("purchaseCode") String code
    ){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        PurchaseDto purchaseDto = purchaseMapper.toDto(purchaseService.findPurchaseByCode(code));

        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("purchase", purchaseDto);
        model.addAttribute("tabTitle", "Sales");

        return "history/purchaseDetails";
    }
}
