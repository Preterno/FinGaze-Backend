package com.fingaze.stock_backend.dto;

public class TopPerformerDto {
    private String stockSymbol;
    private double percentageGain;

    public TopPerformerDto(String stockSymbol, double percentageGain) {
        this.stockSymbol = stockSymbol;
        this.percentageGain = percentageGain;
    }

    public String getStockSymbol() { return stockSymbol; }
    public double getPercentageGain() { return percentageGain; }
}
