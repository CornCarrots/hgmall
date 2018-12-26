package com.lh.hgmall.service;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.dao.ProductDAO;
import com.lh.hgmall.dao.ProductImageDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "product")
public class ProductService {
    @Autowired
    ProductDAO productDAO;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StoreService storeService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ReviewService reviewService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Product> list()
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Product> listByStatus(int status)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productDAO.findAllByStatus(status,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Product> listBySale(int status)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"sales");
        return productDAO.findAllByStatus(status,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Product> listByCidAndStatus(int cid,int status)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productDAO.findAllByCidAndStatus(cid,status,sort);
    }

    public List<Product> listByCidAndKeyAndStatus(int cid,int status,String key)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productDAO.findAllByCidAndStatusAndNameContaining(cid,status,key,sort);
    }

    public List<Product> listByKeyAndStatus(int status,String key)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return productDAO.findAllByStatusAndNameContaining(status,key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Product> listByCidAndStatus(int start,int size,int number,int cid,int status)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Product> page = productDAO.findAllByCidAndStatus(cid,status,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());
        return new PageUtil<>(page,number);
    }

    @CacheEvict(allEntries = true)
    public void add(Product product)
    {
        productDAO.save(product);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        productDAO.delete(id);
        propertyValueService.deleteAllByProduct(id);
        productImageService.delete(id);
    }

    public void deleteAllByCategory(int id)
    {
        ProductService productService = SpringContextUtils.getBean(ProductService.class);
        List<Product> products = listByCidAndStatus(id,0);
        for (Product product:
             products) {
            productService.delete(product.getId());
        }
    }

    @CacheEvict(allEntries = true)
    public void update(Product product)
    {
        productDAO.save(product);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Product get(int id)
    {
        return productDAO.findOne(id);
    }

    public void fillCategoryAndStore(Product product){
        int cid = product.getCid();
        Category category = categoryService.get(cid);
        categoryService.fillStore(category);
//        System.out.println(category);
//        System.out.println(category.getStore());
        product.setCategory(category);
        Store store = category.getStore();
        storeService.fill(store);
        product.setStore(store);
    }

    public void fillCategoryAndStore(List<Product> products){
        ProductService productService = SpringContextUtils.getBean(ProductService.class);
        for (Product product:
                products) {
            productService.fillCategoryAndStore(product);
        }
    }

    public void fillImage(Product product)
    {

        List<ProductImage> productImages = productImageService.listByPid(product.getId());
        if(productImages.size()!=0)
            product.setProductImages(productImages);
        productImages = productImageService.listByPidAndType(product.getId(), ProductImageDAO.type_detail);
        if(productImages.size()!=0)
            product.setProductDetailImages(productImages);
        productImages = productImageService.listByPidAndType(product.getId(), ProductImageDAO.type_single);
        if(productImages.size()!=0)
            product.setProductSingleImages(productImages);

    }

    public void fillFirst(Product product){
        List<ProductImage> productImages = productImageService.listByPidAndType(product.getId(), ProductImageDAO.type_single);
        if(productImages.size()!=0)
        product.setFirstProductImage(productImages.get(0));

    }

    public void fillFirst(List<Product> products)
    {
        ProductService productService = SpringContextUtils.getBean(ProductService.class);

        for (Product product:
                products) {
            productService.fillFirst(product);
        }
    }

    public void fillReview(Product product){
        List<Review> reviews = reviewService.listByProduct(product.getId());
        reviewService.fill(reviews);
        product.setReviewList(reviews);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Map<String, Object>> listForTree(int sid) {
        ProductService productService = SpringContextUtils.getBean(ProductService.class);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        if(sid==0)
            categories = categoryService.listByType(CategoryDAO.type_product);
        else
        {
            categories = categoryService.listByTypeAndSid(CategoryDAO.type_product,sid);
            Store store = storeService.get(sid);
            categories.add(categoryService.get(store.getCid()));
        }
        categoryService.fillProduct(categories);
        categoryService.fillStore(categories);
        int i = 0;
        int pid = -1;
        int cid = -1;
        boolean found = false;
        for (Category category :
                categories) {
            Map<String, Object> map = new HashMap<>();
            if(category.getSid()!=0)
                map.put("parent_id", category.getStore().getCid());
            else
                map.put("parent_id", 0);
            map.put("layer_name", category.getName());
            map.put("EquIndex",i );
            map.put("id", category.getId());
            Map<String, Object> state = new HashMap<>();
            map.put("selectable",false );
            if(category.getProducts()!=null&&category.getSid()!=0)
            {
                if (!found)
                {
                    if (category.getSid()!=0)
                    {
                        pid = category.getStore().getCid();
                        cid = category.getId();
                        state.put("selected",false);
                        state.put("expanded",true);
                        found = true;
                    }
                }
            }
            else {
                if (category.getId()==pid)
                {
                    pid = category.getSid();
                    state.put("expanded",true);
                }
                else
                {
                    state.put("selected",false);
                    state.put("expanded",false);
                }
            }

            map.put("state",state);
            list.add(map);
            i++;
        }
        found=false;
        List<Product> products = productService.listByStatus(0);
        for (Product product:
             products) {
            Map<String, Object> map = new HashMap<>();
            map.put("parent_id", product.getCid());
            map.put("layer_name", product.getName());
            map.put("EquIndex",i );
            map.put("id", product.getId());
            Map<String, Object> state = new HashMap<>();
            if (!found)
            {
                if (product.getCid()==cid)
                {
                    state.put("selected",true);
                    state.put("expanded",true);
                    found = true;
                }
                else {
                    state.put("selected",false);
                    state.put("expanded",false);
                }
            }
            else {
                    state.put("selected",false);
                    state.put("expanded",false);
            }
            map.put("selectable",true );
            map.put("state",state);
            list.add(map);
            i++;
        }
        return list;
    }

}
