package com.fingaze.stock_backend.controller;

import com.fingaze.stock_backend.entity.Stock;
import com.fingaze.stock_backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStockById(@PathVariable Long id) {
        stockService.deleteStockById(id);
        return ResponseEntity.ok("Stock with ID " + id + " deleted successfully.");
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getStockDetails(@RequestParam String symbol) {
        return ResponseEntity.ok(stockService.getStockDetails(symbol));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addStock(@RequestBody Stock stock) {
        stockService.addStock(stock);
        return ResponseEntity.ok("Stock added successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody Map<String, Object> stockUpdateData) {
        stockService.updateStock(stockUpdateData);
        return ResponseEntity.ok("Stock updated successfully.");
    }
}
