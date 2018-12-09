package com.lh.hgmall.service;

import com.lh.hgmall.bean.Exchange;
import com.lh.hgmall.dao.ExchangeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "exchange")
public class ExchangeService {
    @Autowired
    ExchangeDAO exchangeDAO;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Exchange getByOrder(int oid)
    {
        return exchangeDAO.findByOid(oid);
    }

    @CacheEvict(allEntries = true)
    public void add(Exchange exchange){
        exchangeDAO.save(exchange);
    }

    @CacheEvict(allEntries = true)
    public void update(Exchange exchange){
        exchangeDAO.save(exchange);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        exchangeDAO.delete(id);
    }
}
