package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.*;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import com.example.stockexchangebackend.repositories.SectorRepository;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import com.example.stockexchangebackend.repositories.StockPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SectorServiceImpl implements SectorService {
    @Autowired
    SectorRepository sectorRepository;
    @Autowired
    CompanyStockexchangemapRepository companyStockexchangemapRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Override
    public List<PriceResponse> getSectorStockPriceDate(String name, Date FromDate, Date ToDate, String exchangename) throws ParseException {
        Sector sector = sectorRepository.findBySectorName(name);
        List<Company>companies=sector.getCompanies();
        List<PriceResponse>reslist = new ArrayList<>();
        Map<String, Float> map = new HashMap<>();
        Map<String,Integer>countmap= new HashMap<>();
        List<StockPrice>data = stockPriceRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(Company c: companies)
        {

            CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(c.getCompanyName(),exchangename);
            for(StockPrice d: data)
            {
                if(d.getCompanycode().equals(cm.getCompanyCode()) && d.getStockExchange().getName().equals(exchangename) && df.parse(df.format(d.getDate())).compareTo(FromDate)>=0
                        && df.parse(df.format(d.getDate())).compareTo(ToDate)<=0 )
                {
                    float value=d.getShareprice();
                    Integer count = 1;
                    if(map.containsKey(df.format(d.getDate()))) {
                        value = map.get(df.format(d.getDate()));
                        value= value+d.getShareprice();
                        count = countmap.get(df.format(d.getDate()));
                        count=count+1;
                    }
                    countmap.put(df.format(d.getDate()),count);
                    map.put(df.format(d.getDate()),value);
                }
            }

        }
        Date current = FromDate;
        Date end = ToDate;
        while (current.before(end)) {

            float val =0.0f;
            int div= 1;
            if(map.containsKey(df.format(current)))
            {
                val=map.get(df.format(current));
            }
            if(countmap.containsKey(df.format(current)))
            {
                div= countmap.get(df.format(current));
            }
            val= val/div;
            reslist.add(new PriceResponse(df.format(current),val));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, 1);
            current = calendar.getTime();
        }
        float num= 0.0f;
        int avg=1;
        if(map.containsKey(df.format(end)))
        {
            num=map.get(df.format(end));
        }
        if(countmap.containsKey(df.format(end)))
        {
            avg= countmap.get(df.format(end));
        }
        num=num/avg;
        reslist.add(new PriceResponse(df.format(end),num));
        return reslist;
    }

    @Override
    public List<PriceResponse> getSectorStockPriceYear(String name, Date FromDate, Date ToDate, String exchangename) throws ParseException {
        Sector sector = sectorRepository.findBySectorName(name);
        List<Company>companies=sector.getCompanies();
        List<PriceResponse>reslist = new ArrayList<>();
        Map<String, Float> map = new HashMap<>();
        Map<String,Integer>countmap= new HashMap<>();
        List<StockPrice>data = stockPriceRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy");
        for(Company c: companies)
        {

            CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(c.getCompanyName(),exchangename);
            for(StockPrice d: data)
            {
                if(d.getCompanycode().equals(cm.getCompanyCode()) && d.getStockExchange().getName().equals(exchangename) && df.parse(df.format(d.getDate())).compareTo(FromDate)>=0
                        && df.parse(df.format(d.getDate())).compareTo(ToDate)<=0 )
                {
                    float value=d.getShareprice();
                    Integer count = 1;
                    if(map.containsKey(df.format(d.getDate()))) {
                        value = map.get(df.format(d.getDate()));
                        value= value+d.getShareprice();
                        count = countmap.get(df.format(d.getDate()));
                        count=count+1;
                    }
                    countmap.put(df.format(d.getDate()),count);
                    map.put(df.format(d.getDate()),value);
                }
            }

        }
        Date current = FromDate;
        Date end = ToDate;
        while (current.before(end)) {

            float val =0.0f;
            int div= 1;
            if(map.containsKey(df.format(current)))
            {
                val=map.get(df.format(current));
            }
            if(countmap.containsKey(df.format(current)))
            {
                div= countmap.get(df.format(current));
            }
            val= val/div;
            reslist.add(new PriceResponse(df.format(current),val));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.YEAR, 1);
            current = calendar.getTime();
        }
        float num= 0.0f;
        int avg=1;
        if(map.containsKey(df.format(end)))
        {
            num=map.get(df.format(end));
        }
        if(countmap.containsKey(df.format(end)))
        {
            avg= countmap.get(df.format(end));
        }
        num=num/avg;
        reslist.add(new PriceResponse(df.format(end),num));
        return reslist;
    }

}
