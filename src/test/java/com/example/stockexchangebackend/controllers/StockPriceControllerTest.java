package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.models.StockPrice;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import com.example.stockexchangebackend.repositories.StockPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockPriceControllerTest {

    @InjectMocks
    StockPriceController stockPriceController;

    @Mock
    StockPriceRepository stockPriceRepository;
    @Mock
    CompanyStockexchangemapRepository companyStockexchangemapRepository;
    @Mock
    StockExchangeRepository stockExchangeRepository;
    @Test
    void addStockPrice() throws ParseException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        HashMap<String,String> map=new HashMap<>();
        map.put("pricePerShare","100.0");
        map.put("exchangeName","BSE");
        map.put("CompanyCode","F100");
        map.put("Date","2020-01-17");
        map.put("Time","12:50:02");
        //doReturn(any(StockPrice.class)).when(stockPriceRepository.save(any(StockPrice.class)));
        CompanyStockexchangemap cs = new CompanyStockexchangemap();
        StockExchange st = new StockExchange();
        when(companyStockexchangemapRepository.findByCompanyCodeAndStockExchange(map.get("CompanyCode"),map.get("exchangeName"))).thenReturn(cs);
        when(stockExchangeRepository.findByName(map.get("exchangeName"))).thenReturn(st);

        String response= stockPriceController.addStockPrice(map);
        Mockito.verify(stockPriceRepository,times(1)).save(any(StockPrice.class));
        assertThat(response).isEqualTo("Done");
    }
}