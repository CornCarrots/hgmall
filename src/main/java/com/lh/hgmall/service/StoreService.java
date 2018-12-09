package com.lh.hgmall.service;

import com.lh.hgmall.bean.Category;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.OrderItem;
import com.lh.hgmall.bean.Store;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.dao.StoreDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "store")
public class StoreService {
    @Autowired
    StoreDAO storeDAO;
    @Autowired
    CategoryService categoryService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Store> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return storeDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Store> listByStatus(int status) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return storeDAO.findByStatusNot(status, sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Store> listByStatusAndCategory(int status, int cid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return storeDAO.findByStatusNotAndCid(status, cid, sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Store> listByStatus(int start, int size, int number, int status) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Store> page = storeDAO.findByStatusNot(status, pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    public List<Store> listByStatusAndKey(int status, String key) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return storeDAO.findByStatusNotAndNameContaining(status, key, sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Store store) {
        storeDAO.save(store);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        storeDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Store store) {
        storeDAO.save(store);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Store get(int id) {
        return storeDAO.findOne(id);
    }

    public boolean hasExits(String name) {
        StoreService storeService = SpringContextUtils.getBean(StoreService.class);
        for (Store store :
                storeService.list()) {
            if (store.getName().equals(name))
                return true;
        }
        return false;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Store hasExitsEmail(String email) {
        StoreService storeService = SpringContextUtils.getBean(StoreService.class);
        for (Store store :
                storeService.list()) {
            if (store.getEmail().equals(email))
                return store;
        }
        return null;
    }

    public void fill(Store store) {
        int id = store.getCid();
        store.setCategory(categoryService.get(id));
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, store.getId());
        categoryService.fillProduct(categories);
        store.setCategories(categories);
    }

    public void fill(List<Store> stores) {
        StoreService storeService = SpringContextUtils.getBean(StoreService.class);

        for (Store store :
                stores) {
            storeService.fill(store);
        }
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Store> showOrderItem(List<OrderItem> items) {
        List<Store> stores = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(items.get(0));
        int sid = items.get(0).getSid();
        if(items.size()==1)
        {
            Store store = get(sid);
            store.setOrderItems(orderItems);
            stores.add(store);
            return stores;
        }
        for (int i = 1; i < items.size(); i++) {
            int sid1 = items.get(i).getSid();
            if (i == items.size() - 1) {
                if (sid1 != sid) {
                    Store store = get(sid);
                    store.setOrderItems(orderItems);
                    stores.add(store);
                    orderItems = new ArrayList<>();
                    orderItems.add(items.get(i));
                    store = get(sid1);
                    store.setOrderItems(orderItems);
                    stores.add(store);
                } else {
                    orderItems.add(items.get(i));
                    Store store = get(sid);
                    store.setOrderItems(orderItems);
                    stores.add(store);
                }

            } else {
                if (sid1 == sid)
                    orderItems.add(items.get(i));
                else {
                    Store store = get(sid);
                    store.setOrderItems(orderItems);
                    stores.add(store);
                    orderItems = new ArrayList<>();
                    orderItems.add(items.get(i));
                    sid = sid1;
                }
            }
        }
        return stores;
    }

}
