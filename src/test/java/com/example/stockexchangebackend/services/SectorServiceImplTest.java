package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.PriceResponse;
import com.example.stockexchangebackend.models.Sector;
import com.example.stockexchangebackend.repositories.SectorRepository;
import com.example.stockexchangebackend.repositories.StockPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SectorServiceImplTest {

    @InjectMocks
    SectorServiceImpl sectorService;

    @Mock
    SectorRepository sectorRepository;

    @Mock
    StockPriceRepository stockPriceRepository;

    @Test
    void getSectorStockPriceDate() throws ParseException {
        Sector sec = new Sector("XYZ","");
        List<Company> clist = new ArrayList<>();
        sec.setCompanies(clist);
        Mockito.when(stockPriceRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sectorRepository.findBySectorName("XYZ")).thenReturn(sec);
        List<PriceResponse>resp= sectorService.getSectorStockPriceDate("XYZ",new Date(),new Date(),"ABC");
        assertThat(resp).isNotNull();
    }

    @Test
    void getSectorStockPriceYear() throws ParseException {
        Sector sec = new Sector("XYZ","");
        List<Company> clist = new ArrayList<>();
        sec.setCompanies(clist);
        Mockito.when(stockPriceRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sectorRepository.findBySectorName("XYZ")).thenReturn(sec);
        List<PriceResponse>resp= sectorService.getSectorStockPriceYear("XYZ",new Date(),new Date(),"ABC");
        assertThat(resp).isNotNull();
    }
}