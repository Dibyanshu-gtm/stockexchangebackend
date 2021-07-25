package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.*;
import com.example.stockexchangebackend.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {
    @InjectMocks
    CompanyServiceImpl companyService;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    CompanyStockexchangemapRepository companyStockexchangemapRepository;

    @Mock
    StockExchangeRepository stockExchangeRepository;
    @Mock
    StockPriceRepository stockPriceRepository;
    @Mock
    IPODetailRepository ipoDetailRepository;
    @Mock
    SectorRepository sectorRepository;

    @Test
    void listAll() {
        List<Company>c = new ArrayList<>();
        c.add(new Company());
        Mockito.when(companyRepository.findAll()).thenReturn(c);
        List<Company>list= companyService.listAll();
        assertThat(list).isNotNull();

    }

    @Test
    void addCompany() throws ParseException {
        StockExchange exchange= new StockExchange();
        List<CompanyStockexchangemap>clist = new ArrayList<>();
        List<IPODetail>ilist= new ArrayList<>();
        exchange.setCompstockmap(clist);
        exchange.setIpoDetail(ilist);
        Mockito.when(stockExchangeRepository.findByName("")).thenReturn(exchange);
        companyService.addCompany("",0.0,"","",
                "","2002-04-10 09:30:05",0.0f,0L,"","","");
        Mockito.verify(companyRepository,times(1)).save(any(Company.class));
    }

    @Test
    void getIPODetails() {
        Company c = new Company();
        c.setIpo(new IPODetail(0.0,100L,new Date()));
        Mockito.when(companyRepository.findByName("XYZ")).thenReturn(c);
        List<IPODetail>ipList = companyService.getIPODetails("XYZ");
        assertThat(ipList).isNotNull();
    }


    @Test
    void getCompanyStockPriceDate() {
        CompanyStockexchangemap cm = new CompanyStockexchangemap();
        cm.setCompanyCode("Z");
        List<StockPrice>stList = new ArrayList<>();
       Mockito.when(stockPriceRepository.findAll()).thenReturn(stList);
        List<PriceResponse>response= companyService.getCompanyStockPriceDate("XYZ",new Date(),new Date(),"ABC");
        assertThat(response).isNotNull();
    }

    @Test
    void getCompanyStockPriceYear() throws ParseException {
        CompanyStockexchangemap cm = new CompanyStockexchangemap();
        cm.setCompanyCode("Z");
        List<StockPrice>stList = new ArrayList<>();
        Mockito.when(stockPriceRepository.findAll()).thenReturn(stList);
        List<PriceResponse>response= companyService.getCompanyStockPriceYear("XYZ",new Date(),new Date(),"ABC");
        assertThat(response).isNotNull();
    }

    @Test
    void getCompanyStockPriceTime() {
        CompanyStockexchangemap cm = new CompanyStockexchangemap();
        cm.setCompanyCode("Z");
        List<StockPrice>stList = new ArrayList<>();
        Mockito.when(stockPriceRepository.findAll()).thenReturn(stList);
        List<PriceResponse>response= companyService.getCompanyStockPriceTime("XYZ",new Date(),"ABC");
        assertThat(response).isNotNull();
    }


    @Test
    void editCompany() {
        Mockito.when(companyRepository.findByCompanyId(1L)).thenReturn(new Company());
        Company c=companyService.editCompany(1L,"",0.0,"","","");
        Mockito.verify(companyRepository,times(1)).save(any(Company.class));

    }

    @Test
    void getCompanies() {

        List<CompanyResponse>pcList= companyService.getCompanies();
        assertThat(pcList).isNotNull();
    }

    @Test
    void deleteCompany() {
        Company c= new Company();
        Sector sc= new Sector("","");
        IPODetail ipo= new IPODetail(0.0,1000L,new Date());
        ipo.setId(1L);
        c.setIpo(ipo);
        c.setSector(sc);
        List<Company>cmList = new ArrayList<>();
        cmList.add(c);
        Mockito.when(sectorRepository.findBySectorName("")).thenReturn(sc);
        Mockito.when(companyRepository.getById(1L)).thenReturn(c);


        companyService.deleteCompany(1L);
        Mockito.verify(companyRepository,times(1)).deleteById(any(Long.class));
    }
}