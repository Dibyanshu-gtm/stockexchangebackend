package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.IPODetail;
import com.example.stockexchangebackend.models.IPOResponse;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.repositories.IPODetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class IPODetailControllerTest {

    @InjectMocks
    IPODetailController ipoDetailController;
    @Mock
    IPODetailRepository ipoDetailRepository;
    @Test
    void getIpoDetailwhenNull() {
        List<IPODetail>ip = new ArrayList<>();
        Mockito.when(ipoDetailRepository.findAll()).thenReturn(ip);
        ResponseEntity<List<IPOResponse>> ipolist= ipoDetailController.getIpoDetail();
        assertThat(ipolist.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void getIpoDetailwhenNotNull() {
        List<IPODetail>ip = new ArrayList<>();
        Date date = new Date();
        IPODetail ipo = new IPODetail(0.0, 0L,date);
        StockExchange s= new StockExchange();
        List<StockExchange>stlist = new ArrayList<>();
        stlist.add(s);
        ipo.setStockExchange(stlist);
        ipo.setCompany(new Company());
        ip.add(ipo);
        Mockito.when(ipoDetailRepository.findAll()).thenReturn(ip);
        ResponseEntity<List<IPOResponse>> ipolist= ipoDetailController.getIpoDetail();
        assertThat(ipolist.getStatusCodeValue()).isEqualTo(200);
        assertThat(ipolist).isNotNull();

    }

    @Test
    void getIpoById(){
        Date date = new Date();
        IPODetail ipo = new IPODetail(0.0, 0L,date);
        StockExchange s= new StockExchange();
        List<StockExchange>stlist = new ArrayList<>();
        stlist.add(s);
        ipo.setStockExchange(stlist);
        ipo.setCompany(new Company());
        Mockito.when(ipoDetailRepository.findById(1L)).thenReturn(java.util.Optional.of(ipo));
        ResponseEntity<?>rp = ipoDetailController.getIpoById(1L);
        assertThat(rp.getStatusCodeValue()).isEqualTo(200);
        assertThat(rp).isNotNull();
    }

}