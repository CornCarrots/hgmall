package com.lh.hgmall.service;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.ManagerRole;
import com.lh.hgmall.bean.Role;
import com.lh.hgmall.bean.Store;
import com.lh.hgmall.dao.ManagerDAO;
import com.lh.hgmall.util.EncodeUtil;
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
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "manager")
public class ManagerService {
    @Autowired
    ManagerDAO managerDAO;
    @Autowired
    ManagerRoleService managerRoleService;
    @Autowired
    RoleService roleService;
    @Autowired
    StoreService storeService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Manager> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Manager> listByStore(int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerDAO.findAllBySid(sid,sort);
    }

    public List<Manager> listByKey(String key) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerDAO.findAllByNameContaining(key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Manager> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = managerDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    public int init(Store store)
    {
        ManagerService managerService = SpringContextUtils.getBean(ManagerService.class);
        Manager manager = new Manager();
        manager.setName(store.getName());
        Map<String,Object> map = EncodeUtil.encode(store.getIdentification().substring(12,18));
        manager.setPassword(map.get("pass").toString());
        manager.setSalt(map.get("salt").toString());
        manager.setSid(store.getId());
        manager.setNickName(store.getName()+"管理员");
        manager.setCreateDate(new Date());
        manager.setMobile("");
        manager.setEmail("");
        manager.setSex(0);
        manager.setStatus(1);
        managerService.add(manager);
        ManagerRole managerRole = new ManagerRole();
        managerRole.setMid(manager.getId());
        managerRole.setRid(13);
        managerRoleService.add(managerRole);
        int id = manager.getId();
        return id;
    }

    @CacheEvict(allEntries = true)
    public void add(Manager manager) { managerDAO.save(manager); }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        managerDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Manager manager)
    {
        managerDAO.save(manager);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Manager get(int id)
    {
        return managerDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Manager> listByName(String name){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerDAO.findAllByName(name,sort);}

    public Manager getByName(String name){
        for (Manager manager:
                list()) {
            if(manager.getName().equals(name))
                return manager;
        }
        return null;
    }

    public Manager getByEmail(String email){
        for (Manager manager:
                list()) {
            if(manager.getEmail().equals(email))
                return manager;
        }
        return null;
    }


    public void fill(Manager manager)
    {
        List<Role> roles = new ArrayList<>();
        List<ManagerRole> managerRoles = managerRoleService.listByManager(manager.getId());
        for (ManagerRole managerRole:
             managerRoles) {
            int rid = managerRole.getRid();
            roles.add(roleService.get(rid));
        }
        manager.setRoles(roles);
        int sid = manager.getSid();
        if(sid==0)
        {
            Store store = new Store();
            store.setName("无");
            manager.setStore(store);
        }
        else
           manager.setStore(storeService.get(sid));
    }

    public void fill(List<Manager> managers)
    {
        ManagerService managerService = SpringContextUtils.getBean(ManagerService.class);
        for (Manager manager:
             managers) {
            managerService.fill(manager);
        }
    }
}
