package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.purchase.CreatePurchaseRequest;
import com.tavarlabs.pos.entity.Purchase;

import java.util.List;

public interface PurchaseService {
    List<Purchase> getAllPurchases();
    Purchase createPurchase(CreatePurchaseRequest PurchaseRequest);
    Purchase findPurchaseByCode(String code);
}
