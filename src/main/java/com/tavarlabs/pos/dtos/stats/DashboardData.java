package com.tavarlabs.pos.dtos.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardData {
    private BigDecimal totalTodaySales;
    private long availableItems;
    private long runningLowItems;
    private long outOfStockItems;
    private long soldItems;
}
