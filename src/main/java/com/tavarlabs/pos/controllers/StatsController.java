package com.tavarlabs.pos.controllers;

import com.tavarlabs.pos.dtos.stats.DashboardData;
import com.tavarlabs.pos.dtos.stats.MonthlySum;
import com.tavarlabs.pos.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/dashboard/data")
    public ResponseEntity<DashboardData> getDashboardData(
            //This way spring will not throw an error if I don't give start and end values
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end
    ){
         DashboardData dd = statsService.getDashboardData(start, end);
         return ResponseEntity.ok(dd);
    }

}
