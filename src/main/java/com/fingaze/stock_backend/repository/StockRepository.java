package com.fingaze.stock_backend.repository;

import com.fingaze.stock_backend.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean existsByStockSymbol(String stockSymbol);
}

