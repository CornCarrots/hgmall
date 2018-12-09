package com.lh.hgmall.service;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.ManagerRole;
import com.lh.hgmall.dao.ManagerRoleDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
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
import java.util.Set;

@Service
@CacheConfig(cacheNames = "manager_role")
public class ManagerRoleService {
    @Autowired
    ManagerRoleDAO managerRoleDAO;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ManagerRole> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerRoleDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ManagerRole> listByManager(int mid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerRoleDAO.findAllByMid(mid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<ManagerRole> listByRole(int rid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return managerRoleDAO.findAllByRid(rid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<ManagerRole> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = managerRoleDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @CacheEvict(allEntries = true)
    public void add(ManagerRole managerRole)
    {
        managerRoleDAO.save(managerRole);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        managerRoleDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void deleteByManager(int mid)
    {
        for (ManagerRole managerRole:
             listByManager(mid)) {
            managerRoleDAO.delete(managerRole.getId());
        }

    }

    @CacheEvict(allEntries = true)
    public void update(ManagerRole managerRole)
    {
        managerRoleDAO.save(managerRole);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public ManagerRole get(int id)
    {
        return managerRoleDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public ManagerRole getByRoleAndManager(int rid,int mid)
    {
        return managerRoleDAO.findByRidAndMid(rid,mid);
    }

}
