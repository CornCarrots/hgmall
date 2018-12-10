package com.lh.hgmall.service;

import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.dao.UserDAO;
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

import java.util.List;

@Service
@CacheConfig(cacheNames = "user")
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    OrderService orderService;
    @Autowired
    MemberService memberService;

    Sort sort = new Sort(Sort.Direction.DESC,"id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<User> list()
    {
        return userDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<User> list(int start,int size,int number)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page<User> page = userDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    public List<User> listByKey(String key)
    {
        return userDAO.findAllByNameContaining(key,sort);
    }

    public List<User> listByKeyAndMember(String key,int mid)
    {
        return userDAO.findAllByNameContainingAndMidNot(key,mid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<User> listMember(int start,int size,int number,int mid)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page<User> page = userDAO.findAllByMidNot(mid,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());
        return new PageUtil<>(page,number);
    }

    @CacheEvict(allEntries = true)
    public void add(User user){ userDAO.save(user);}

    @CacheEvict(allEntries = true)
    public void delete(int id){userDAO.delete(id);}

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public User get(int id)
    {
        return userDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public User get(String name){return userDAO.findAllByName(name,sort);}

    @CacheEvict(allEntries = true)
    public void update(User user)
    {
        userDAO.save(user);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public User getByName(String name){
        UserService userService = SpringContextUtils.getBean(UserService.class);
        for (User user:
             userService.list()) {
            if(user.getName().equals(name))
                return user;
        }
        return null;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public User getByEmail(String email){
        UserService userService = SpringContextUtils.getBean(UserService.class);

        for (User user:
             userService.list()) {
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public void fillOrder(User user)
    {
        List<Order> orders = orderService.listByUser(user.getId());
        List<Order> expenses = orderService.listByUserAndType(user.getId(),'0');
        if(orders!=null)
            user.setExpenses(expenses);
        List<Order> recharges = orderService.listByUserAndType(user.getId(),'1');
        if(orders!=null)
            user.setRecharges(recharges);
    }

    public void fillOrder(List<User> users)
    {
        UserService userService = SpringContextUtils.getBean(UserService.class);

        for (User user:
             users) {
            userService.fillOrder(user);
        }
    }

    public void fillMember(User user)
    {
        int mid = user.getMid();
        if(mid==0)
            return;
        user.setMember(memberService.get(mid));
    }

    public void fillMember(List<User> users)
    {
        UserService userService = SpringContextUtils.getBean(UserService.class);

        for (User user:
                users) {
            userService.fillMember(user);
        }
    }
}
