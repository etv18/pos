package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.stats.MonthlySum;
import com.tavarlabs.pos.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/stats")
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/monthly/sales/totals/{year}")
    public ResponseEntity<List<MonthlySum>> monthlySalesTotals(
            @PathVariable("year") int year
    ){
        List<MonthlySum> result = statsService.monthlySalesTotals(year);
        return ResponseEntity.ok(result);
    }

}
