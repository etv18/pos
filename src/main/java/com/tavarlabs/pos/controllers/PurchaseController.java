package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.purchase.CreatePurchaseRequest;
import com.tavarlabs.pos.dtos.purchase.PurchaseDto;
import com.tavarlabs.pos.entity.Purchase;
import com.tavarlabs.pos.mappers.PurchaseMapper;
import com.tavarlabs.pos.services.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/purchases")
public class PurchaseController {
    /* TODO: replace purchase related objects with purchases */

    private final PurchaseService purchaseService;
    private final PurchaseMapper purchaseMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<PurchaseDto>> getAllPurchases(){
        List<Purchase> purchases = purchaseService.getAllPurchases();
        List<PurchaseDto> purchaseDtos = purchases.stream().map(purchaseMapper::toDto).toList();
        return ResponseEntity.ok(purchaseDtos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PurchaseDto> createPurchase(
            @Valid @RequestBody CreatePurchaseRequest createPurchaseRequest
    ){
        Purchase purchase = purchaseService.createPurchase(createPurchaseRequest);
        PurchaseDto purchaseDto = purchaseMapper.toDto(purchase);
        return ResponseEntity.ok(purchaseDto);
    }
}
