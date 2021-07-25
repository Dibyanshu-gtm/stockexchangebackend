package com.example.stockexchangebackend.controllers;


import com.example.stockexchangebackend.models.*;
import com.example.stockexchangebackend.repositories.CompanyRepository;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import com.example.stockexchangebackend.repositories.IPODetailRepository;
import com.example.stockexchangebackend.repositories.StockExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class IPODetailController {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    StockExchangeRepository stockExchangeRepository;
    @Autowired
    IPODetailRepository ipoDetailRepository;
    @Autowired
    CompanyStockexchangemapRepository companyStockexchangemapRepository;
    @RequestMapping(value = "/ipo",method = RequestMethod.POST)
    public String addIPODetail(@RequestBody Map<String,String> text) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= formatter.parse(text.get("openDateTime"));
        Company company = companyRepository.findByName(text.get("companyName"));
        StockExchange exchange= stockExchangeRepository.findByName(text.get("exchangename").trim());
        CompanyStockexchangemap cmp= new CompanyStockexchangemap();
        if(Objects.isNull(company))
        {
            return " Company not found";
        }
        if(Objects.isNull(exchange))
        {
            return "Exchange not found";
        }
        IPODetail ipo = ipoDetailRepository.findByCompanyName(text.get("companyName"));
        if(Objects.isNull(ipo)) {
            IPODetail ipoDetail = new IPODetail(Double.parseDouble(text.get("pricePerShare")), Long.parseLong(text.get("totalNumberOfShares")), date);
            ipoDetail.setCompany(company);
            List<StockExchange> st = new ArrayList<>();
            st.add(exchange);
            ipoDetail.setStockExchange(st);
            ipoDetailRepository.save(ipoDetail);
        }
        else{

            //Check if Exchange is new or old
            //checking if already registered
            CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(company.getCompanyName(), exchange.getName());
            //if that company and exchange never registered
            if(Objects.isNull(cm))
            {
                cm= companyStockexchangemapRepository.findByCompany(company.getCompanyName());
                cmp.setCompanyCode(cm.getCompanyCode());
                cmp.setCompany(company);
                cmp.setStockExchange(exchange);
                companyStockexchangemapRepository.save(cmp);
                List<IPODetail>ipList= new ArrayList<>();
                if(!exchange.getIpoDetail().isEmpty())
                {
                    ipList=exchange.getIpoDetail();
                }
                ipo.setPricePerShare(Double.parseDouble(text.get("pricePerShare")));
                ipo.setTotalNumberOfShares(Long.parseLong(text.get("totalNumberOfShares")));
                ipo.setOpenDateTime(date);
                ipo.setCompany(company);
                ipoDetailRepository.save(ipo);
                ipList.add(ipo);
                exchange.setIpoDetail(ipList);
                stockExchangeRepository.save(exchange);
            }
            else
            {
                ipo.setPricePerShare(Double.parseDouble(text.get("pricePerShare")));
                ipo.setTotalNumberOfShares(Long.parseLong(text.get("totalNumberOfShares")));
                ipo.setOpenDateTime(date);
                ipoDetailRepository.save(ipo);
            }
        }
        return "Done";
    }
    @RequestMapping(value = "/ipodetails",method = RequestMethod.GET)
    public ResponseEntity<List<IPOResponse>>getIpoDetail()
    {
        List<IPODetail>ipoList= ipoDetailRepository.findAll();
        List<IPOResponse>ipList = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(IPODetail ipo: ipoList)
        {
            String ex="";
            for(StockExchange e :ipo.getStockExchange())
            {
                ex=ex+e.getName()+",";
            }
            IPOResponse ip = new IPOResponse(ipo.getId(),ipo.getCompany().getCompanyName(),ipo.getPricePerShare(),
                    ipo.getTotalNumberOfShares(),formatter.format(ipo.getOpenDateTime()),ex);
            ipList.add(ip);
        }

        if(ipList.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<IPOResponse>>(ipList,HttpStatus.OK);
    }
    @RequestMapping(value = "/ipodetails/{id}",method = RequestMethod.GET)
    public ResponseEntity<?>getIpoById(@PathVariable Long id)
    {
        IPODetail c = ipoDetailRepository.findById(id).get();
        String ex="";
        for(StockExchange e :c.getStockExchange())
        {
            ex=ex+e.getName()+",";
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        IPOResponse ip = new IPOResponse(c.getId(),c.getCompany().getCompanyName(),c.getPricePerShare(),
                c.getTotalNumberOfShares(),formatter.format(c.getOpenDateTime()),ex);
        if(Objects.isNull(ip))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  ResponseEntity.ok().body(ip);
    }

}
