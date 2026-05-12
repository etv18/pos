package com.tavarlabs.pos.services;

import com.tavarlabs.pos.dtos.stats.DashboardData;
import com.tavarlabs.pos.dtos.stats.MonthlySum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    Long countProductsWithLowStock(int minimumStock);
    Long countOutOfStockProducts();
    Long countAvailableStock();
    BigDecimal totalSalesOfACertainTimeFrame(LocalDateTime start, LocalDateTime end);
    List<MonthlySum> monthlySalesTotals(int year);
    DashboardData getDashboardData(LocalDateTime start, LocalDateTime end);
}
