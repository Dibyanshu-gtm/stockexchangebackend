package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CompanyStockExchangeMapControllerTest {
    @Mock
    CompanyStockExchangeMapController companyStockExchangeMapController;

    @Test
    void getCompanyName() {

        ResponseEntity<?> responseEntity=companyStockExchangeMapController.getCompanyName("","");
        assertThat(responseEntity).isEqualTo(null);

    }
}