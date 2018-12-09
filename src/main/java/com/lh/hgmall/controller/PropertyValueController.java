package com.lh.hgmall.controller;

import com.lh.hgmall.bean.ProductImage;
import com.lh.hgmall.bean.Property;
import com.lh.hgmall.bean.PropertyValue;
import com.lh.hgmall.dao.ProductImageDAO;
import com.lh.hgmall.service.PropertyService;
import com.lh.hgmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    PropertyService propertyService;

    @GetMapping(value = "admin/categories/{cid}/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid,@PathVariable("cid")int cid) throws Exception
    {
            List<Property> properties = propertyService.listByCid(cid);
            if(properties.size()==0)
                return null;
        for (Property property:
             properties) {
            propertyValueService.init(property);
        }
        List<PropertyValue> propertyValues = propertyValueService.listByProduct(pid);
        propertyValueService.fill(propertyValues);
        return propertyValues;
    }

    @PutMapping(value = "admin/categories/{cid}/products/{pid}/propertyValues/{id}")
    public void update(@RequestBody PropertyValue propertyValue)throws Exception
    {
        propertyValueService.update(propertyValue);
    }


}
