package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import com.example.stockexchangebackend.models.StockPrice;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import com.example.stockexchangebackend.repositories.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class StockPriceController {
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    CompanyStockexchangemapRepository companyStockexchangemapRepository;
    @Autowired
    StockExchangeRepository stockExchangeRepository;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/price",method = RequestMethod.POST)
    public String addStockPrice(@RequestBody Map<String,String> text) throws ParseException {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate= new Date();

        CompanyStockexchangemap cmp =companyStockexchangemapRepository.findByCompanyCodeAndStockExchange(text.get("CompanyCode").trim(),text.get("exchangeName").trim());
        Date date = dateFormat.parse(text.get("Date"));
        Date time = timeFormat.parse(text.get("Time"));
        StockPrice stockPrice= new StockPrice(text.get("CompanyCode").trim(),date,time,Float.parseFloat(text.get("pricePerShare")),currentDate);
        stockPrice.setCompany(cmp.getCompany());
        stockPrice.setStockExchange(stockExchangeRepository.findByName(text.get("exchangeName").trim()));
        stockPriceRepository.save(stockPrice);
        return "Done";
    }



}
