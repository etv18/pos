package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.purchase.CreatePurchaseRequest;
import com.tavarlabs.pos.dtos.purchaseline.CreatePurchaseLineRequest;
import com.tavarlabs.pos.entity.Purchase;
import com.tavarlabs.pos.entity.PurchaseLine;
import com.tavarlabs.pos.entity.Product;
import com.tavarlabs.pos.repositories.PurchaseLineRepository;
import com.tavarlabs.pos.repositories.PurchaseRepository;
import com.tavarlabs.pos.repositories.ProductRepository;
import com.tavarlabs.pos.services.PurchaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseLineRepository purchaseLineRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAllWithLines();
        return purchases;
    }

    @Override
    public Purchase createPurchase(CreatePurchaseRequest purchaseRequest) {
        // Checks
        if(purchaseRequest == null)
            throw new IllegalArgumentException("Purchase request has no content");

        if(purchaseRequest.getLines().isEmpty())
            throw new IllegalArgumentException("This purchase request has lines inside");

        // Starting missing dependencies
        Purchase newPurchase = new Purchase();
        String code = generatePurchaseCode();
        List<PurchaseLine> lines = generatePurchaseLines(purchaseRequest.getLines(), newPurchase);

        // Setting dependencies
        newPurchase.setCode(code);
        newPurchase.setSupplierName(purchaseRequest.getSupplierName());
        newPurchase.setLines(lines);

        return purchaseRepository.save(newPurchase);
    }

    @Override
    public Purchase findPurchaseByCode(String code) {
        return purchaseRepository.findByCode(code).orElseThrow(
                () -> new EntityNotFoundException("Purchase with code " + code + "was not found")
        );
    }

    private List<PurchaseLine> generatePurchaseLines(List<CreatePurchaseLineRequest> requestedLines, Purchase purchase){
        List<PurchaseLine> lines = new ArrayList<>();
        Product product = null;
        for(CreatePurchaseLineRequest requestedLine : requestedLines){
            product = productRepository.findByCode(requestedLine.getProductCode())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Product not found. Code = [ "+requestedLine.getProductCode() + " ]"
                    ));

            increaseProductStock(product, requestedLine.getQuantity());

            PurchaseLine purchaseLine = new PurchaseLine();
            purchaseLine.setQuantity(requestedLine.getQuantity());
            purchaseLine.setPurchase(purchase);
            purchaseLine.setProduct(product);
            lines.add(purchaseLine);

            productRepository.save(product);
        }
        return lines;
    }

    private String generatePurchaseCode() {
        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        long count = purchaseRepository.count();

        String code;
        int attempts = 0;

        do {
            long sequence = count + 1 + attempts;
            code = datePart + String.format("%02d", sequence);
            attempts++;

        } while (purchaseRepository.existsByCode(code) && attempts < 20);

        if (purchaseRepository.existsByCode(code)) {
            throw new RuntimeException("Could not generate unique purchase code after 20 attempts");
        }

        return code;
    }

    private void increaseProductStock(Product product, int quantity){
        if(quantity < 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        product.setStock(product.getStock() + quantity);
    }
}
