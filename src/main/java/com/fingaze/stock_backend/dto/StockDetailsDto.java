package com.fingaze.stock_backend.dto;

public class StockDetailsDto {
    private String stockSymbol;
    private String stockName;
    private double oldPrice;
    private int quantity;

    // Constructors, getters, and setters
    public StockDetailsDto(String stockSymbol, String stockName, double oldPrice, int quantity) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
