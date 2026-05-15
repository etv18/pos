package com.tavarlabs.pos.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthViewController {

    @GetMapping("/login")
    public String viewLogin(Model model){
        return "/auth/login";
    }

    @GetMapping("/unauthorized")
    public String unauthorizedPage() {
        return "auth/unauthorized";
    }
}
