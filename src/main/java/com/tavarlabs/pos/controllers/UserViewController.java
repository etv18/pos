package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.mappers.UserMapper;
import com.tavarlabs.pos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserViewController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/create")
    public String createView(Model model){
        model.addAttribute("tabTitle", "User");
        model.addAttribute(
                "users",
                userService.getAllUsers().stream()
                        .map(user -> userMapper.toResponseDto(user))
        );
        return "user/index";
    }
}
