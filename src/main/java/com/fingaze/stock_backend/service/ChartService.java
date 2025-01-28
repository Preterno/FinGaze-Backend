package com.fingaze.stock_backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fingaze.stock_backend.entity.Stock;
import com.fingaze.stock_backend.repository.StockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class ChartService {

    @Autowired
    private StockRepository stockRepository;

    Dotenv dotenv = Dotenv.load();
    private final String FLASK_API_URL = dotenv.get("FLASK_API_BASE_URL") + "historical_data";
    private final String API_KEY = dotenv.get("FLASK_API_KEY");

    public Map<String, List<Double>> getChartData() {
        LocalDate today = LocalDate.now();

        // Define time intervals
        Map<String, Integer> intervals = Map.of(
                "3_months", 90);

        // Fetch stocks from the repository
        List<Stock> stocks = stockRepository.findAll();
        List<String> stockSymbols = stocks.stream()
                .map(Stock::getStockSymbol)
                .collect(Collectors.toList());

        // Fetch historical prices from Flask API
        Map<String, Map<LocalDate, Double>> historicalPrices = fetchHistoricalPricesFromApi(stockSymbols);

        // Initialize chart data map
        Map<String, List<Double>> chartData = new HashMap<>();
        for (String interval : intervals.keySet()) {
            chartData.put(interval, new ArrayList<>(Collections.nCopies(intervals.get(interval) + 1, 0.0)));
        }

        // Calculate portfolio value for each interval
        for (String interval : intervals.keySet()) {
            int days = intervals.get(interval);
            List<Double> values = chartData.get(interval);

            for (int i = 0; i <= days; i++) {
                LocalDate date = today.minusDays(i);
                double dailyPortfolioValue = 0.0;

                for (Stock stock : stocks) {
                    String stockSymbol = stock.getStockSymbol();
                    if (historicalPrices.containsKey(stockSymbol)) {
                        Double price = historicalPrices.get(stockSymbol).get(date);
                        if (price != null
                                && (stock.getInvestDate().isBefore(date) || stock.getInvestDate().isEqual(date))) {
                            dailyPortfolioValue += price * stock.getNoOfStocks();
                        } 
                    }
                }

                values.set(i, dailyPortfolioValue);
            }
        }

        return chartData;
    }

    private Map<String, Map<LocalDate, Double>> fetchHistoricalPricesFromApi(List<String> stockSymbols) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Map<LocalDate, Double>> historicalPrices = new HashMap<>();

        try {
            String symbols = String.join(",", stockSymbols);
            String apiUrl = FLASK_API_URL + "?stocks=" + symbols;

            // Add Authorization Header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make API call
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class);

            String responseBody = response.getBody();

            if (responseBody != null) {
                // Deserialize using ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Map<String, Double>> responseMap = objectMapper.readValue(
                        responseBody,
                        new TypeReference<Map<String, Map<String, Double>>>() {
                        });

                // Convert string dates to LocalDate
                for (String stock : stockSymbols) {
                    Map<String, Double> stockData = responseMap.get(stock);
                    if (stockData != null) {
                        Map<LocalDate, Double> datePriceMap = stockData.entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> LocalDate.parse(e.getKey()),
                                        Map.Entry::getValue));
                        historicalPrices.put(stock, datePriceMap);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return historicalPrices;
    }
}
