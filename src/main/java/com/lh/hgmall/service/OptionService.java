package com.lh.hgmall.service;

import com.lh.hgmall.bean.Option;
import com.lh.hgmall.dao.OptionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "option")
public class OptionService {
    @Autowired
    OptionDAO optionDAO;

    Sort sort = new Sort(Sort.Direction.DESC,"id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Option> list()
    {
        return optionDAO.findAll(sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Option option){
        optionDAO.save(option);
    }

    @CacheEvict(allEntries = true)
    public void update(Option option){
        optionDAO.save(option);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        optionDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Option get(int id){
        return optionDAO.findOne(id);
    }
}
