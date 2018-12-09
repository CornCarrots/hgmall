package com.lh.hgmall.service;

import com.lh.hgmall.bean.Product;
import com.lh.hgmall.bean.ProductImage;
import com.lh.hgmall.dao.ProductImageDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.SpringContextUtils;
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
@CacheConfig(cacheNames = "productImage")
public class ProductImageService {
    @Autowired
    ProductImageDAO productImageDAO;
    @Autowired
    ProductService productService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ProductImage> list()
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productImageDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ProductImage> listByPid(int pid){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productImageDAO.findAllByPid(pid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ProductImage> listByPidAndType(int pid,String type){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productImageDAO.findAllByPidAndType(pid,type,sort);
    }

    @CacheEvict(allEntries = true)
    public void add(ProductImage productImage)
    {
        productImageDAO.save(productImage);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        productImageDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(ProductImage productImage)
    {
        productImageDAO.save(productImage);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public ProductImage get(int id)
    {
        return productImageDAO.findOne(id);
    }

    public void fill(ProductImage image)
    {
        int pid = image.getPid();
        image.setProduct(productService.get(pid));
    }

    public void fill(List<ProductImage> images)
    {
        ProductImageService productImageService = SpringContextUtils.getBean(ProductImageService.class);
        for (ProductImage image:
             images) {
            productImageService.fill(image);
        }
    }

}
