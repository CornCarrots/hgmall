package com.lh.hgmall.service;

import com.lh.hgmall.bean.Power;
import com.lh.hgmall.dao.PowerDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "power")
public class PowerService {
    @Autowired
    PowerDAO powerDAO;

    Sort sort = new Sort(Sort.Direction.DESC, "id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Power> list() {
        return powerDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Power> listByMember(int mid) {
        return powerDAO.findAllByMid(mid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Power> listByMemberNot(int mid) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return powerDAO.findAllByMidNot(mid,sort);
    }

    public List<Power> listByMemberAndKey(int mid,String key) {
        return powerDAO.findAllByMidAndAndTitleContaining(mid,key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Power> listByMember(int start, int size, int number, int mid)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page page = powerDAO.findAllByMid(mid,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<Power>(page,number);
    }

    @CacheEvict(allEntries = true)
    public void add(Power power)
    {
        powerDAO.save(power);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        powerDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Power power)
    {
        powerDAO.save(power);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Power get(int id)
    {
        return powerDAO.findOne(id);
    }
}
