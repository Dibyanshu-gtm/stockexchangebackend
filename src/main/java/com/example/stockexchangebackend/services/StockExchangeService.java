package com.example.stockexchangebackend.services;


import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.StockExchange;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StockExchangeService {
    public String addExchange(StockExchange stockExchange);
    public List<StockExchange> getExchange();
    public List<Company>getCompanies(String exchangename);

}
