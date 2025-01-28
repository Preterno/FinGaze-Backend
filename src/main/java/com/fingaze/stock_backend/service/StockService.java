package com.fingaze.stock_backend.service;

import com.fingaze.stock_backend.entity.Stock;
import com.fingaze.stock_backend.repository.StockRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private FinnhubService finnhubService;

    public void deleteStockById(Long id) {
        stockRepository.deleteById(id);
    }

    public void addStock(Stock stock) {
        if (stockRepository.existsByStockSymbol(stock.getStockSymbol())) {
            throw new RuntimeException("Stock with symbol " + stock.getStockSymbol() + " already exists.");
        }

        stock.setInvestDate(LocalDate.now());
        stockRepository.save(stock);
    }

    public Map<String, Object> getStockDetails(String symbol) {
        try {
            var stockData = finnhubService.getStockData(symbol);
            Map<String, Object> stockDetails = new HashMap<>();
            stockDetails.put("stockSymbol", symbol);
            stockDetails.put("stockName", stockData.getString("name"));
            stockDetails.put("currentPrice", stockData.getDouble("currentPrice"));
            stockDetails.put("marketCap", stockData.getString("formattedMarketCap"));
            return stockDetails;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock details: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        List<Map<String, Object>> stockDetailsList = new ArrayList<>();

        for (Stock stock : stocks) {
            Map<String, Object> stockDetails = new HashMap<>();
            stockDetails.put("id", stock.getId());
            stockDetails.put("stockSymbol", stock.getStockSymbol());
            stockDetails.put("stockName", stock.getStockName());
            stockDetails.put("investDate", stock.getInvestDate());
            stockDetails.put("oldPrice", stock.getOldPrice());
            stockDetails.put("noOfStocks", stock.getNoOfStocks());

            try {
                JSONObject liveData = finnhubService.getStockData(stock.getStockSymbol());
                double currentPrice = liveData.getDouble("currentPrice");
                double change = ((currentPrice - stock.getOldPrice()) / stock.getOldPrice()) * 100;

                stockDetails.put("currentPrice", String.format("%.2f", currentPrice));
                stockDetails.put("change", String.format("%.1f", change));
                stockDetails.put("marketCapitalization", liveData.getString("formattedMarketCap"));
            } catch (Exception e) {
                stockDetails.put("currentPrice", "Error fetching data");
                stockDetails.put("change", "Error fetching data");
                stockDetails.put("marketCapitalization", "Error fetching data");
            }

            stockDetailsList.add(stockDetails);
        }

        return stockDetailsList;
    }

    public void updateStock(Map<String, Object> stockUpdateData) {
        Long id = ((Number) stockUpdateData.get("id")).longValue();
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock with ID " + id + " not found."));

        stock.setNoOfStocks((int) stockUpdateData.get("noOfStocks"));
        stock.setOldPrice((double) stockUpdateData.get("currentPrice"));
        stock.setInvestDate(LocalDate.parse((String) stockUpdateData.get("currentDate")));
        stockRepository.save(stock);
    }

    public List<String> getTopPerformers() {
        List<Stock> stocks = stockRepository.findAll();
        List<Map<String, Object>> stockDetailsList = new ArrayList<>();

        for (Stock stock : stocks) {
            try {
                JSONObject liveData = finnhubService.getStockData(stock.getStockSymbol());
                double currentPrice = liveData.getDouble("currentPrice");
                double change = ((currentPrice - stock.getOldPrice()) / stock.getOldPrice()) * 100;

                Map<String, Object> stockDetails = new HashMap<>();
                stockDetails.put("stockSymbol", stock.getStockSymbol());
                stockDetails.put("stockName", stock.getStockName());
                stockDetails.put("currentPrice", currentPrice);
                stockDetails.put("change", change);
                stockDetailsList.add(stockDetails);
            } catch (Exception e) {
                // Log and skip on failure
            }
        }

        // Sort stocks by change in descending order and pick the top 5
        stockDetailsList.sort((a, b) -> Double.compare((double) b.get("change"), (double) a.get("change")));
        return stockDetailsList.stream()
                .limit(5)
                .map(stock -> "Name: " + stock.get("stockName") +
                        ", Symbol: " + stock.get("stockSymbol") +
                        ", Change: " + String.format("%.1f", stock.get("change")) + "%" +
                        ", Price: " + String.format("%.2f", stock.get("currentPrice")))
                .toList();
    }
}
