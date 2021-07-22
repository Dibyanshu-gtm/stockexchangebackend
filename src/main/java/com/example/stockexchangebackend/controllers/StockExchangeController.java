package com.example.stockexchangebackend.controllers;


import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.StockExchange;

import com.example.stockexchangebackend.services.StockExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class StockExchangeController {
    @Autowired
    StockExchangeService stockExchangeService;
    @RequestMapping(value="/exchange", method = RequestMethod.POST)
    public String addExchange(@RequestBody StockExchange stockExchange){

        return stockExchangeService.addExchange(stockExchange);
    }
    @RequestMapping(value="/exchange", method = RequestMethod.GET)
    public ResponseEntity<List<StockExchange>> getStockExchanges(){
        List<StockExchange>exchanges= stockExchangeService.getExchange();
        if(exchanges.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<StockExchange>>(exchanges,HttpStatus.OK);
    }
    @RequestMapping(value = "/exchange/companies",method = RequestMethod.GET)
    public ResponseEntity<List<Company>>getCompanies(@RequestParam(name = "exchangename")String exchangename){
        List<Company>companies= stockExchangeService.getCompanies(exchangename);
        if(companies.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Company>>(companies,HttpStatus.OK);
    }
    @RequestMapping(value = "/stipo/{name}",method =RequestMethod.GET)
    public String getipo(@PathVariable String  name)
    {
        return stockExchangeService.getipodetails(name);
    }
}
