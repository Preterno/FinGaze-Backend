package com.fingaze.stock_backend.controller;

import com.fingaze.stock_backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/top-performers")
public class TopPerformersController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<String>> getTopPerformers() {
        return ResponseEntity.ok(stockService.getTopPerformers());
    }
}
