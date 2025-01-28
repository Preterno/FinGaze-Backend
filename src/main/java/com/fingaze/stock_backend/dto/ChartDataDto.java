package com.fingaze.stock_backend.dto;

public class ChartDataDto {
    private String date;
    private double portfolioValue;

    public ChartDataDto(String date, double portfolioValue) {
        this.date = date;
        this.portfolioValue = portfolioValue;
    }

    public String getDate() {
        return date;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }
}
