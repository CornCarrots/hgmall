package com.lh.hgmall.service;

import com.lh.hgmall.bean.Category;
import com.lh.hgmall.bean.Property;
import com.lh.hgmall.bean.PropertyValue;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.dao.PropertyDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@CacheConfig(cacheNames = "property")
public class PropertyService {
    @Autowired
    PropertyDAO propertyDAO;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyValueService propertyValueService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Property> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return propertyDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Property> listByCid(int start, int size, int number, int cid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Property> page = propertyDAO.findAllByCid(cid, pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<Property>(page, number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Property> listByCid(int cid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return propertyDAO.findAllByCid(cid, sort);
    }

    public List<Property> listByCidAndKey(int cid, String key) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return propertyDAO.findAllByCidAndNameContaining(cid, key, sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Property property) {
        propertyDAO.save(property);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        propertyDAO.delete(id);
        propertyValueService.deleteAllByProperty(id);
    }

    public void deleteAllByCategory(int id)
    {
        PropertyService propertyService = SpringContextUtils.getBean(PropertyService.class);
        List<Property> properties = listByCid(id);
        for (Property property:
             properties) {
            propertyService.delete(property.getId());
        }
    }

    @CacheEvict(allEntries = true)
    public void update(Property property) {
        propertyDAO.save(property);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Property get(int id) {
        return propertyDAO.findOne(id);
    }

    public void fill(Property property) {
        int cid = property.getCid();
        property.setCategory(categoryService.get(cid));
    }

    public void fill(List<Property> properties) {
        PropertyService propertyService = SpringContextUtils.getBean(PropertyService.class);

        for (Property property :
                properties) {
            propertyService.fill(property);
        }
    }
}