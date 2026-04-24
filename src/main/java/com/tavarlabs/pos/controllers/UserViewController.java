package com.tavarlabs.pos.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {
    @GetMapping("/create")
    public String createView(Model model){
        model.addAttribute("tabTitle", "User");
        return "user/index";
    }
}
