package com.lh.hgmall.service;

import com.lh.hgmall.bean.Operation;
import com.lh.hgmall.dao.OperationDAO;
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
@CacheConfig(cacheNames = "operation")
public class OperationService {
    @Autowired
    OperationDAO operationDAO;

    Sort sort = new Sort(Sort.Direction.ASC, "id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Operation> list() {
        return operationDAO.findAll(sort);
    }

    public List<Operation> listByKey(String key) {
        return operationDAO.findAllByNameContaining(key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Operation> list(int start, int size, int number) {
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = operationDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @CacheEvict(allEntries = true)
    public void add(Operation operation)
    {
        operationDAO.save(operation);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        operationDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Operation operation)
    {
        operationDAO.save(operation);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Operation get(int id)
    {
        return operationDAO.findOne(id);
    }
}
