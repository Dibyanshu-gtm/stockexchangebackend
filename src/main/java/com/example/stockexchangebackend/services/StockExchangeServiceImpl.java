package com.example.stockexchangebackend.services;



import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import com.example.stockexchangebackend.models.IPODetail;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockExchangeServiceImpl implements StockExchangeService{
    @Autowired
    StockExchangeRepository stockExchangeRepository;
    @Override
    public String addExchange(StockExchange stockExchange) {
        stockExchangeRepository.save(stockExchange);
        return "Done "+ stockExchange.getName()+" is Added";
    }

    @Override
    public List<StockExchange> getExchange() {
        List<StockExchange>exchange= stockExchangeRepository.findAll();
        return exchange;
    }

    @Override
    public List<Company> getCompanies(String exchangename) {
        StockExchange stockExchange= stockExchangeRepository.findByName(exchangename);
        List<CompanyStockexchangemap> compstockmap= stockExchange.getCompstockmap();
        List<Company>comp = new ArrayList<>();
        for(CompanyStockexchangemap c: compstockmap)
        {
            Company company= new Company(c.getCompany().getCompanyName(),c.getCompany().getTurnover(),c.getCompany().getCeo(),
                    c.getCompany().getBoardOfDirectors(),c.getCompany().getCompanyBrief());
            company.setId(c.getCompany().getId());
            comp.add(company);
        }
        return  comp;
    }


}
