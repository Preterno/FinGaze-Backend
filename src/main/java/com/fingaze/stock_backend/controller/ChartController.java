package com.fingaze.stock_backend.controller;

import com.fingaze.stock_backend.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @GetMapping
    public ResponseEntity<Map<String, List<Double>>> getChartData() {
        return ResponseEntity.ok(chartService.getChartData());
    }
}
