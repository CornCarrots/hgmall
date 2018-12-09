package com.lh.hgmall.service;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.PermissionDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@CacheConfig(cacheNames = "permission")
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;
    @Autowired
    ModuleService moduleService;
    @Autowired
    ManagerService managerService;
    @Autowired
    OperationService operationService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    LogService logService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Permission> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return permissionDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Permission> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = permissionDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @CacheEvict(allEntries = true)
    public void add(Permission permission)
    {
        permissionDAO.save(permission);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        permissionDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void delete(int mid,int oid)
    {
        Permission permission = get(oid,mid);
        permissionDAO.delete(permission.getId());
    }

    @CacheEvict(allEntries = true)
    public void update(Permission permission)
    {
        permissionDAO.save(permission);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Permission get(int id)
    {
        return permissionDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Permission get(int oid,int mid)
    {
        return permissionDAO.findByOidAndMid(oid,mid);
    }

    public void fill(List<Permission> permissions){
        PermissionService permissionService = SpringContextUtils.getBean(PermissionService.class);
        for (Permission permission:
             permissions) {
            permissionService.fill(permission);
        }
    }

    public void fill(Permission permission){
        int mid = permission.getMid();
        int oid = permission.getOid();
        permission.setModule(moduleService.get(mid));
        permission.setOperation(operationService.get(oid));
    }

    public boolean needInterceptor(String url) {
        List<String> urls = moduleService.listURL();
        for (String url1 :
                urls) {
            if (url.equals(url1)||url.equals("/admin/"))
                return true;
        }
        return false;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public HashMap<String, List<Operation>> formatPermission(List<Permission> permissions) {
        HashMap<String, List<Operation>> map = new HashMap<>();
        for (Permission permission :
                permissions) {
            String s = "";
            Module module = permission.getModule();
            Operation operation = permission.getOperation();
            if (moduleService.hasChild(module))
            {
                if(module.getPid()==0)
                    s=module.getUrl()+"/";
                else
                    s = module.getUrl();
            }
            else
            {
                s = moduleService.getChildURL(module.getUrl());
            }
            if (map.containsKey(s)) {
                List<Operation> list = map.get(s);
                list.add(operation);
                map.put(s,list);
            }
            else
            {
                List<Operation> list = new ArrayList<>();
                list.add(operation);
                map.put(s,list);
            }
        }
        return map;
    }

    public boolean hasPermission(String name, String url, String method, Manager manager) {
        PermissionService permissionService = SpringContextUtils.getBean(PermissionService.class);

        List<Permission> permissions = rolePermissionService.listPermissionByManager(name);
//        System.out.println("---------------------------");
        permissionService.fill(permissions);
        HashMap<String, List<Operation>> map = permissionService.formatPermission(permissions);
        for (String key :
                map.keySet()) {
            if (key.equals(url)) {
                for (Operation val:
                        map.get(key)) {
                    if (val.getName().equals(method))
                    {
                        String uri = url.substring(url.lastIndexOf("/"),url.length());
                        List<Module> modules = moduleService.getByURL(uri);
                        Module module = null;
                        if(modules.size()==1)
                            module = modules.get(0);
                        else
                        {
                            String parent = url.substring(0,url.indexOf(uri));
                            parent = parent.substring(parent.lastIndexOf("/"),parent.length());
                            Module module1 = moduleService.getByURL(parent).get(0);
                            for (Module module2:
                                 modules) {
                                if(module2.getPid()==module1.getPid())
                                    module=module1;
                            }
                        }
                        Log log = new Log();
                        log.setCreateDate(new Date());
                        log.setUid(manager.getId());
                        log.setText("在"+module.getDesc()+"界面"+val.getDesc());
                        logService.add(log);
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
