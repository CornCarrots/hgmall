package com.lh.hgmall.service;

import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.OrderItem;
import com.lh.hgmall.bean.Product;
import com.lh.hgmall.dao.OrderItemDAO;
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

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "orderItem")
public class OrderItemService {
    @Autowired
    OrderItemDAO orderItemDAO;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<OrderItem> listByOrder(int oid)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        return orderItemDAO.findAllByOid(oid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<OrderItem> listByUserAndOrder(int uid,int oid)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"sid");
        return orderItemDAO.findAllByUidAndOid(uid,oid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<OrderItem> listByIds(List<String> ids)
    {
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            items.add(get(Integer.parseInt(ids.get(i))));
        }
        return items;
    }

    @CacheEvict(allEntries = true)
    public void add(OrderItem orderItem){
        orderItemDAO.save(orderItem);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        orderItemDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(OrderItem orderItem){
        orderItemDAO.save(orderItem);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public OrderItem get(int id){
     return orderItemDAO.findOne(id);
    }

    public void fill(OrderItem orderItem)
    {
        int pid = orderItem.getPid();
        Product product = productService.get(pid);
        productService.fillFirst(product);
        productService.fillCategoryAndStore(product);
        orderItem.setProduct(product);
        int uid = orderItem.getUid();
        orderItem.setUser(userService.get(uid));
    }

    public void fill(List<OrderItem> orderItems)
    {
        OrderItemService orderItemService = SpringContextUtils.getBean(OrderItemService.class);
        for (OrderItem orderItem:
             orderItems) {
            orderItemService.fill(orderItem);
        }
    }

    public void fillOrder(OrderItem orderItem)
    {
        int oid = orderItem.getOid();
        Order order = orderService.get(oid);
        orderItem.setOrder(order);
    }

}
