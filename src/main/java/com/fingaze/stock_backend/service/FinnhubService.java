package com.fingaze.stock_backend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.cdimascio.dotenv.Dotenv;

@Service
public class FinnhubService {
    Dotenv dotenv = Dotenv.load();
    private String apiKey = dotenv.get("FINNHUB_API_KEY");

    public JSONObject getStockData(String stockSymbol) throws Exception {
        // Quote endpoint for current price
        String quoteUrl = "https://finnhub.io/api/v1/quote?symbol=" + stockSymbol + "&token=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        String quoteResponse = restTemplate.getForObject(quoteUrl, String.class);
        JSONObject quoteJson = new JSONObject(quoteResponse);
        double currentPrice = quoteJson.optDouble("c", 0.0);

        // Profile2 endpoint for name and market cap
        String profileUrl = "https://finnhub.io/api/v1/stock/profile2?symbol=" + stockSymbol + "&token=" + apiKey;
        String profileResponse = restTemplate.getForObject(profileUrl, String.class);
        JSONObject profileJson = new JSONObject(profileResponse);
        double marketCapitalization = profileJson.optDouble("marketCapitalization", 0.0);
        String name = profileJson.optString("name");

        JSONObject stockData = new JSONObject();
        stockData.put("name", name);
        stockData.put("currentPrice", currentPrice);
        stockData.put("formattedMarketCap", formatMarketCap(marketCapitalization));

        return stockData;
    }

    private String formatMarketCap(double marketCap) {
        if (marketCap >= 1_000_000) {
            return String.format("%.2fT", marketCap / 1_000_000);
        } else if (marketCap >= 1_000) {
            return String.format("%.2fB", marketCap / 1_000);
        } else {
            return String.format("%.2fM", marketCap);
        }
    }
}
