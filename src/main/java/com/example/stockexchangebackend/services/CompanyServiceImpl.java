package com.example.stockexchangebackend.services;

import com.example.stockexchangebackend.models.*;
import com.example.stockexchangebackend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    StockExchangeRepository stockExchangeRepository;
    @Autowired
    SectorRepository sectorRepository;
    @Autowired
    IPODetailRepository ipoDetailRepository;
    @Autowired
    StockPriceRepository stockPriceRepository;
    @Autowired
    CompanyStockexchangemapRepository companyStockexchangemapRepository;

    @Override
    public List<Company> listAll() {
        List<Company>companies= companyRepository.findAll();
        return companies;
    }
    @Override
    public String addCompany(String companyName, Double turnover,String ceo, String boardOfDirectors,String companyBrief,
                             String openDateTime,Float pricePerShare,Long totalNumberOfShares, String CompanyCode, String sector,
                             String exchangeName
    ) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate= new Date();

        CompanyStockexchangemap cmp= new CompanyStockexchangemap();
        Company company= new Company(companyName,turnover,ceo,boardOfDirectors,companyBrief);
        Date date= formatter.parse(openDateTime);

        IPODetail ipoDetail= new IPODetail(Double.parseDouble(String.valueOf(pricePerShare)), totalNumberOfShares,date);
        Date datetime= dateFormat.parse(openDateTime);
        Date time = timeFormat.parse(timeFormat.format(date));
        StockPrice stockPrice= new StockPrice(CompanyCode,datetime,time,pricePerShare,currentDate);
        StockExchange stockExchange=stockExchangeRepository.findByName(exchangeName);
        List<CompanyStockexchangemap> compList = new ArrayList<>();
        List<IPODetail>ipList= new ArrayList<>();
        if(!stockExchange.getCompstockmap().isEmpty()) {
            compList=stockExchange.getCompstockmap();
        }
        if(!stockExchange.getIpoDetail().isEmpty())
        {
            ipList=stockExchange.getIpoDetail();
        }
        company.setSector(sectorRepository.findBySectorName(sector));
        ipoDetail.setCompany(company);
        cmp.setCompany(company);
        stockPrice.setCompany(company);
        cmp.setCompanyCode(CompanyCode);
        cmp.setStockExchange(stockExchange);
        companyRepository.save(company);
        companyStockexchangemapRepository.save(cmp);
        compList.add(cmp);
        stockExchange.setCompstockmap(compList);
        //List<StockExchange>st = new ArrayList<>();
        //st.add(stockExchange);
        ipoDetailRepository.save(ipoDetail);
        ipList.add(ipoDetail);
        stockExchange.setIpoDetail(ipList);
        //ipoDetail.setStockExchange(st);
        stockPrice.setStockExchange(stockExchange);
        stockExchangeRepository.save(stockExchange);
        stockPriceRepository.save(stockPrice);
        return "Done "+company.getCompanyName()+" is added";
    }

    @Override
    public List<IPODetail> getIPODetails(String name) {
        List<IPODetail>ipolist= new ArrayList<>();
        ipolist.add(companyRepository.findByName(name).getIpo());
        return ipolist;
    }

    @Override
    public List<PriceResponse> getCompanyStockPriceDate(String name, Date FromDate, Date ToDate,String exchangename) {
        CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(name,exchangename);
        List<PriceResponse>reslist = new ArrayList<>();
        Map<String, Float> map = new HashMap<>();
        Map<String,Integer>countmap= new HashMap<>();
        List<StockPrice>data = stockPriceRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(StockPrice d:data)
        {
            if(d.getCompanycode().equals(cm.getCompanyCode()) && d.getStockExchange().getName().equals(exchangename) && d.getDate().compareTo(FromDate)>=0 && d.getDate().compareTo(ToDate)<=0 )
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
    public List<PriceResponse> getCompanyStockPriceYear(String name, Date FromDate, Date ToDate,String exchangename) throws ParseException {
        CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(name,exchangename);
        List<PriceResponse>reslist = new ArrayList<>();
        Map<String, Float> map = new HashMap<>();
        Map<String,Integer>countmap= new HashMap<>();
        List<StockPrice>data = stockPriceRepository.findAll();
        DateFormat df = new SimpleDateFormat("yyyy");
        for(StockPrice d: data)
        {
            if(d.getCompanycode().equals(cm.getCompanyCode()) && d.getStockExchange().getName().equals(exchangename) && df.parse(df.format(d.getDate())).compareTo(FromDate)>=0 && df.parse(df.format(d.getDate())).compareTo(ToDate)<=0 )
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

    @Override
    public List<PriceResponse> getCompanyStockPriceTime(String name, Date date,String exchangename) {
        CompanyStockexchangemap cm= companyStockexchangemapRepository.findByCompanyNameAndStockExchange(name,exchangename);
        List<PriceResponse>reslist = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        List<StockPrice>data = stockPriceRepository.findAll();
        Collections.sort(data,new Comparator<StockPrice>(){
            public int compare(StockPrice s1, StockPrice s2){
                return s1.getTime().compareTo(s2.getTime());
            }
        });
        for(StockPrice d:data)
        {
            if(d.getCompanycode().equals(cm.getCompanyCode()) &&d.getDate().compareTo(date)==0&& d.getStockExchange().getName().equals(exchangename))
            {
                reslist.add(new PriceResponse(df.format(d.getTime()),d.getShareprice()));
            }
        }
        return reslist;
    }

    @Override
    public Company findById(Long id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public Company editCompany(Long id,String companyName, Double turnover, String ceo, String boardOfDirectors, String companyBrief) {
        Company comp= companyRepository.findByCompanyId(id);
        //Company comp= new Company(companyName,turnover,ceo,boardOfDirectors,companyBrief);
        comp.setCompanyName(companyName);
        comp.setTurnover(turnover);
        comp.setCeo(ceo);
        comp.setBoardOfDirectors(boardOfDirectors);
        comp.setCompanyBrief(companyBrief);
        return companyRepository.save(comp);
        //return comp;
    }

    @Override
    public List<CompanyResponse> getCompanies() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponse>resList = new ArrayList<>();
        String ex="";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Company c : companies)
        {
            List<StockExchange>sc = c.getIpo().getStockExchange();
            for(StockExchange s:sc)
            {
                ex=ex+" "+s.getName();
            }
            resList.add(new CompanyResponse(c.getId(),c.getCompanyName(),c.getTurnover(),c.getCeo(),c.getBoardOfDirectors(),c.getCompanyBrief(),c.getIpo().getPricePerShare(),c.getIpo().getTotalNumberOfShares(), formatter.format( c.getIpo().getOpenDateTime()),ex));
            ex="";
        }
        return resList;
    }

    @Override
    public String deleteCompany(Long id) {
        Company company=companyRepository.getById(id);
        IPODetail ipo = company.getIpo();

        String x="Done";

        List<CompanyStockexchangemap>companyStockexchangemapList=companyStockexchangemapRepository.findAll();
        for (CompanyStockexchangemap companyStockexchangemap: companyStockexchangemapList)
        {
            if(companyStockexchangemap.getCompany().equals(company))
            {
                companyStockexchangemap.setCompany(null);
                Long idcheck = companyStockexchangemap.getId();
                //x=x+idcheck+" "+companyStockexchangemap.getStockExchange().getName();
                StockExchange exchange = stockExchangeRepository.findByName(companyStockexchangemap.getStockExchange().getName());
                List<CompanyStockexchangemap>cs=exchange.getCompstockmap();
                List<IPODetail> ipoList = exchange.getIpoDetail();
                cs.remove(companyStockexchangemap);
                ipoList.remove(ipo);
                exchange.setCompstockmap(cs);
                exchange.setIpoDetail(ipoList);

                stockExchangeRepository.save(exchange);
                companyStockexchangemapRepository.deleteById(idcheck);

            }
        }
        List<StockPrice>stockPrices= stockPriceRepository.findAll();
        for(StockPrice st: stockPrices)
        {
            if(st.getCompany().equals(company))
            {
                stockPriceRepository.deleteById(st.getId());
            }
        }
        ipoDetailRepository.deleteById(ipo.getId());
        Sector sec =sectorRepository.findBySectorName(company.getSector().getSectorName());
        List<Company> companies= sec.getCompanies();
        companies.remove(company);
        sec.setCompanies(companies);



        companyRepository.deleteById(id);
        return x;

    }

}
