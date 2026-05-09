package com.tavarlabs.pos.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/dashboard")
public class DashboardViewController {

    @GetMapping
    public String createView(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        //model.addAttribute("username", username.toUpperCase());
        model.addAttribute("tabTitle", "Purchase");
        return "dashboard/dashboard";
    }
}
