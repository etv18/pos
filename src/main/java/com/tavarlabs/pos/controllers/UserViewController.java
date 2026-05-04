package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.enums.RoleName;
import com.tavarlabs.pos.mappers.RoleMapper;
import com.tavarlabs.pos.mappers.UserMapper;
import com.tavarlabs.pos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserViewController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    final String ROLE_ = "ROLE_";

    @GetMapping("/index")
    public String createView(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<String> roles = Arrays.asList(RoleName.values())
                .stream()
                .map(RoleName::name)
                .map(roleNameStr -> roleNameStr.substring(ROLE_.length()))
                .toList();
        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("tabTitle", "User");
        model.addAttribute("roles", roles);
        model.addAttribute(
                "users",
                userService.getAllUsers().stream()
                        .map(user -> userMapper.toResponseDto(user))
        );
        return "user/index";
    }
}
