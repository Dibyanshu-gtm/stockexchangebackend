package com.example.stockexchangebackend.controllers;


import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.CompanyResponse;
import com.example.stockexchangebackend.models.IPODetail;
import com.example.stockexchangebackend.models.PriceResponse;
import com.example.stockexchangebackend.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/company",method = RequestMethod.POST)
    public String addCompany(@RequestBody Map<String,String> text) throws ParseException {

        return companyService.addCompany(text.get("companyName").trim(),Double.parseDouble(text.get("turnover")),
                text.get("ceo").trim(),text.get("boardOfDirectors").trim(),text.get("companyBrief").trim(),text.get("openDateTime"),
                Float.parseFloat(text.get("pricePerShare")),Long.parseLong(text.get("totalNumberOfShares")),text.get("CompanyCode").trim(),
                text.get("sector").trim(),text.get("exchangeName").trim()
        );

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/company",method = RequestMethod.GET)
    public ResponseEntity<List<Company>> getCompanies(){
        List<Company>gr =companyService.listAll();
        if(gr.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Company>>(gr,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/company/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getCompaniesById(@PathVariable Long id){
        Company c =companyService.findById(id);

        if(Objects.isNull(c))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  ResponseEntity.ok().body(c);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping( "/company/{id}")
    public ResponseEntity<Company>updateCompany(@RequestBody Company company){

        Company c= companyService.editCompany(company.getId(),company.getCompanyName(),company.getTurnover(),
                company.getCeo(),company.getBoardOfDirectors(),company.getCompanyBrief());
        if(Objects.isNull(c))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(c);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/companyipo/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<IPODetail>>getIPOCompany(@PathVariable String name){
        List<IPODetail>info= companyService.getIPODetails(name);
        if(info.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<IPODetail>>(info,HttpStatus.OK);
    }


    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value="/getpricedate/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<PriceResponse>>getStockPrice(@PathVariable String name, @RequestParam(name = "from")String from, @RequestParam(name = "todate")String todate, @RequestParam(name="exchangename")String exchangename) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        List<PriceResponse>resp= companyService.getCompanyStockPriceDate(name,dateFormat.parse(from),dateFormat.parse(todate),exchangename);
        if(resp.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<PriceResponse>>(resp,HttpStatus.OK);
    }

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value="/getpriceyear/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<PriceResponse>>getStockPriceyear(@PathVariable String name,@RequestParam(name = "from")String from, @RequestParam(name = "todate")String todate,@RequestParam(name="exchangename")String exchangename) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("yyyy");
        List<PriceResponse>resp= companyService.getCompanyStockPriceYear(name,dateFormat.parse(from),dateFormat.parse(todate),exchangename);
        if(resp.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<PriceResponse>>(resp,HttpStatus.OK);
    }
    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value="/getpricetime/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<PriceResponse>>getStockPriceyear(@PathVariable String name,@RequestParam(name = "from")String from,@RequestParam(name="exchangename")String exchangename) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        List<PriceResponse>resp= companyService.getCompanyStockPriceTime(name,dateFormat.parse(from),exchangename);
        if(resp.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<PriceResponse>>(resp,HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/companies",method = RequestMethod.GET)
    public ResponseEntity<List<CompanyResponse>>getCompaniesUser()
    {
        List<CompanyResponse>companyResponseList= companyService.getCompanies();
        if(companyResponseList.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<CompanyResponse>>(companyResponseList,HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public String deleteCompany(@PathVariable Long id){
        return companyService.deleteCompany(id);
    }


}
