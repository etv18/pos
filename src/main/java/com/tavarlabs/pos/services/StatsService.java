package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.stats.MonthlySum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface StatsService {
    long countProductsWithLowStock(int minimumStock);
    long countOutOfStockProducts();
    List<MonthlySum> monthlySalesTotals(int year);
}
