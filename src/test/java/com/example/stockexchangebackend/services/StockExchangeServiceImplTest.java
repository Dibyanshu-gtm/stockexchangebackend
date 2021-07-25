package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class StockExchangeServiceImplTest {

    @InjectMocks
    StockExchangeServiceImpl stockExchangeService;

    @Mock
    StockExchangeRepository stockExchangeRepository;
    @Test
    void addExchange() {

        String response=stockExchangeService.addExchange(new StockExchange());
        assertThat(response).isEqualTo("Done null is Added");
        Mockito.verify(stockExchangeRepository,Mockito.times(1)).save(any(StockExchange.class));
    }

    @Test
    void getExchange() {
        List<StockExchange> st = stockExchangeService.getExchange();
        assertThat(st).isNotNull();
    }

    @Test
    void getCompanies() {
        StockExchange exchange = new StockExchange();
        exchange.setCompstockmap(new ArrayList<>());
        Mockito.when(stockExchangeRepository.findByName("")).thenReturn(exchange);
        List<Company> companies = stockExchangeService.getCompanies("");
        assertThat(companies).isNotNull();
    }

}