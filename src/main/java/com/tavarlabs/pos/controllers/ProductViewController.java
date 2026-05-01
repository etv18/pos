package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.product.ProductDto;
import com.tavarlabs.pos.mappers.ProductMapper;
import com.tavarlabs.pos.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/stock")
@Controller
public class ProductViewController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/products")
    public String viewProducts(Model model, Authentication authentication){
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        List<ProductDto> products = productService.listProducts().stream().map(productMapper::toDto).toList();
        model.addAttribute("username", username.toUpperCase());
        model.addAttribute("products", products);
        return "/stock/products";
    }
}
