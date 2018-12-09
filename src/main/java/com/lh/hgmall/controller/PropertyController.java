package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Property;
import com.lh.hgmall.bean.PropertyValue;
import com.lh.hgmall.service.CategoryService;
import com.lh.hgmall.service.PropertyService;
import com.lh.hgmall.service.PropertyValueService;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.TreeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PropertyController {
    Logger logger = Logger.getLogger(PropertyController.class);
    @Autowired
    PropertyService propertyService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyValueService propertyValueService;


    @GetMapping(value = "admin/categories/{cid}/properties")
    public PageUtil<Property> list(@PathVariable("cid")int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "9")int size) throws Exception
    {
        start = start<0?0:start;
        PageUtil<Property> page =propertyService.listByCid(start, size, 5,cid);
        List<Property> properties = page.getContent();
        propertyService.fill(properties);
        page.setContent(properties);
        return page;
    }

    @PostMapping(value = "admin/categories/{cid}/properties")
    public void add(@RequestBody Property property)throws Exception
    {
        propertyService.add(property);
    }

    @DeleteMapping(value = "admin/categories/{cid}/properties/{id}")
    public String delete(@PathVariable("id")int id)throws Exception
    {
        Property property = propertyService.get(id);
        propertyValueService.deleteAllByProperty(property.getId());
        propertyService.delete(id);
        return null;
    }

    @GetMapping(value = "admin/categories/{cid}/properties/{id}")
    public Property edit(@PathVariable("id")int id)throws Exception
    {
        Property property =  propertyService.get(id);
        propertyService.fill(property);
        return property;
    }

    @PutMapping(value = "admin/categories/{cid}/properties/{id}")
    public void update(@RequestBody Property property){
        propertyService.update(property);
    }

    @PostMapping(value = "admin/categories/{cid}/properties/search")
    public List<Property> search(@PathVariable("cid")int cid,@RequestParam("key")String key)
    {
        List<Property> properties = propertyService.listByCidAndKey(cid, key);
        propertyService.fill(properties);
        return properties;
    }
}
