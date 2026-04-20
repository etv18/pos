package com.tavarlabs.pos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invoice")
public class InvoiceViewController {

    @GetMapping("/create")
    public String createView(Model model){
        model.addAttribute("tabTitle", "Invoice");
        return "invoice/index";
    }
}
