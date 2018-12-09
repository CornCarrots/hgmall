package com.lh.hgmall.service;

import com.lh.hgmall.bean.Product;
import com.lh.hgmall.bean.Property;
import com.lh.hgmall.bean.PropertyValue;
import com.lh.hgmall.dao.PropertyValueDAO;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "propertyValue")
public class PropertyValueService {
    @Autowired
    PropertyValueDAO propertyValueDAO;
    @Autowired
    ProductService productService;
    @Autowired
    PropertyService propertyService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<PropertyValue> list()
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return propertyValueDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<PropertyValue> listByProduct(int pid)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return propertyValueDAO.findAllByPid(pid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<PropertyValue> listByProperty(int ptid)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return propertyValueDAO.findAllByPtid(ptid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PropertyValue getByProductAndProperty(int pid,int ptid)
    {
        return propertyValueDAO.findPropertyValueByPidAndPtid(pid,ptid);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PropertyValue get(int id)
    {
        return propertyValueDAO.findOne(id);
    }

    public void init(Property property)
    {
        PropertyValueService propertyValueService = SpringContextUtils.getBean(PropertyValueService.class);

        int cid = property.getCid();
        int ptid = property.getId();
        List<Product> products = productService.listByCidAndStatus(cid,0);
        if(products.size()==0)
            return;
        for (Product product:
             products) {
            int pid = product.getId();
            PropertyValue propertyValue = getByProductAndProperty(pid,ptid);
            if(propertyValue==null)
            {
                PropertyValue propertyValue1 = new PropertyValue();
                propertyValue1.setPid(pid);
                propertyValue1.setPtid(ptid);
                propertyValueService.add(propertyValue1);
            }
        }

    }

    @CacheEvict(allEntries = true)
    public void add(PropertyValue propertyValue)
    {
        propertyValueDAO.save(propertyValue);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        propertyValueDAO.delete(id);
    }

    public void deleteAllByProduct(int pid)
    {
        PropertyValueService propertyValueService = SpringContextUtils.getBean(PropertyValueService.class);

        List<PropertyValue> propertyValues = listByProduct(pid);
        for (PropertyValue propertyValue:
                propertyValues) {
            propertyValueService.delete(propertyValue.getId());
        }
    }

    @CacheEvict(allEntries = true)
    public void deleteAllByProperty(int ptid)
    {
        PropertyValueService propertyValueService = SpringContextUtils.getBean(PropertyValueService.class);

        List<PropertyValue> propertyValues = listByProperty(ptid);
        for (PropertyValue propertyValue:
                propertyValues) {
            propertyValueService.delete(propertyValue.getId());
        }
    }

    @CacheEvict(allEntries = true)
    public void update(PropertyValue propertyValue)
    {
        propertyValueDAO.save(propertyValue);
    }

    public void fill(PropertyValue propertyValue)
    {
        int pid = propertyValue.getPid();
        int ptid = propertyValue.getPtid();
        Product product = productService.get(pid);
        propertyValue.setProduct(product);
        Property property = propertyService.get(ptid);
        propertyValue.setProperty(property);
    }

    public void fill(List<PropertyValue> propertyValues)
    {
        PropertyValueService propertyValueService = SpringContextUtils.getBean(PropertyValueService.class);

        for (PropertyValue propertyValue:
             propertyValues) {
            propertyValueService.fill(propertyValue);
        }
    }
}
