package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.PriceResponse;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public interface SectorService {

    public List<PriceResponse>getSectorStockPriceDate(String name, Date FromDate, Date ToDate, String exchangename) throws ParseException;
    public List<PriceResponse>getSectorStockPriceYear(String name, Date FromDate, Date ToDate,String exchangename) throws ParseException;

}
