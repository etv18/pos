package com.tavarlabs.pos.services.impl;

import com.tavarlabs.pos.dtos.stats.MonthlySum;
import com.tavarlabs.pos.repositories.InvoiceRepository;
import com.tavarlabs.pos.repositories.ProductRepository;
import com.tavarlabs.pos.services.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public long countProductsWithLowStock(int minimumStock) {
        if(minimumStock < 1)
            throw new IllegalArgumentException("Minimum stock should be greater than or equals to 1");
        return productRepository.countProductsWithLowStock(minimumStock);
    }

    @Override
    public long countOutOfStockProducts() {
        return productRepository.countProductsOutOfStock();
    }

    @Override
    public List<MonthlySum> monthlySalesTotals(int year) {
        List<MonthlySum> monthlySumList = invoiceRepository.getMonthlySalesTotals(year);
        Map<Integer, BigDecimal> sortedData = new TreeMap<>(); //This implementation saves the key-value pair sorted automatically either alphabetically or numeric way

        for(int i = 1; i <= 12; i++){
            sortedData.put(i, BigDecimal.ZERO);
        }

        monthlySumList.forEach(m -> sortedData.put(m.month(), m.total()));

        return sortedData.entrySet().stream()
                .map(sd -> new MonthlySum(sd.getKey(), sd.getValue()))
                .toList();
    }
}
