package com.example.stockexchangebackend.controllers;


import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import com.example.stockexchangebackend.repositories.CompanyStockexchangemapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class CompanyStockExchangeMapController {
    @Autowired
    CompanyStockexchangemapRepository companyStockexchangemapRepository;

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/"})
    @RequestMapping(value = "/getcompanyname/{code}", method = RequestMethod.GET)
    public ResponseEntity<?>getCompanyName(@PathVariable String code, @RequestParam(name="exchangename") String exchangename) {

        CompanyStockexchangemap cmp =companyStockexchangemapRepository.findByCompanyCodeAndStockExchange(code,exchangename);
        if(Objects.isNull(cmp))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  ResponseEntity.ok().body(new Company(cmp.getCompany().getCompanyName(),cmp.getCompany().getTurnover(),cmp.getCompany().getCeo(),cmp.getCompany().getBoardOfDirectors(),cmp.getCompany().getCompanyBrief()));
    }
}
