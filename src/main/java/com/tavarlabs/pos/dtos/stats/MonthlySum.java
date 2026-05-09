package com.tavarlabs.pos.dtos.stats;

import java.math.BigDecimal;

public record MonthlySum(int month, BigDecimal total) {
}
