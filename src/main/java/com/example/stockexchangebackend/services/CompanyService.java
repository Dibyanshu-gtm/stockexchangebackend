package com.example.stockexchangebackend.services;


import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.IPODetail;
import com.example.stockexchangebackend.models.PriceResponse;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Component
public interface CompanyService {
    public List<Company> listAll();
    public String addCompany(String companyName, Double turnover,String ceo, String boardOfDirectors,String companyBrief,
                             String openDateTime,Float pricePerShare,Long totalNumberOfShares, String CompanyCode, String sector,
                             String exchangeName
    ) throws ParseException;
    public List<IPODetail>getIPODetails(String name);
    public String getipo(String name);
    public List<PriceResponse>getCompanyStockPriceDate(String name, Date FromDate, Date ToDate, String exchangename);
    public List<PriceResponse>getCompanyStockPriceYear(String name, Date FromDate, Date ToDate,String exchangename) throws ParseException;
    public List<PriceResponse>getCompanyStockPriceTime(String name, Date date,String exchangename);
    public Company findById(Long id);
    public Company editCompany(Long id,String companyName, Double turnover,String ceo, String boardOfDirectors,String companyBrief);
}
