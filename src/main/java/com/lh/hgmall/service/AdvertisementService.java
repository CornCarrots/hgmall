package com.lh.hgmall.service;

import com.lh.hgmall.bean.Advertisement;
import com.lh.hgmall.dao.AdvertisementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "advertisement")
public class AdvertisementService {
    @Autowired
    AdvertisementDAO advertisementDAO;

    @Cacheable(keyGenerator = "wiselyKeyGenerator")
    public List<Advertisement> list()
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return advertisementDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Advertisement> listByType(String type)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return advertisementDAO.findAllByType(type,sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Advertisement advertisement)
    {
        advertisementDAO.save(advertisement);
    }

    @CacheEvict(allEntries = true)
    public void update(Advertisement advertisement)
    {
        advertisementDAO.save(advertisement);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        advertisementDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Advertisement get(int id)
    {
        return advertisementDAO.findOne(id);
    }
}
