package com.example.stockexchangebackend.repositories;


import com.example.stockexchangebackend.models.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockExchangeRepository extends JpaRepository<StockExchange,Long> {
    StockExchange findByName(String s);
}
