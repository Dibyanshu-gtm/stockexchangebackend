package com.example.stockexchangebackend.repositories;


import com.example.stockexchangebackend.models.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockPriceRepository extends JpaRepository<StockPrice,Long> {
}
