package com.lh.hgmall.service;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.RolePermissionDAO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@CacheConfig(cacheNames = "role_permission")
public class RolePermissionService {
    @Autowired
    RolePermissionDAO rolePermissionDAO;
    @Autowired
    RoleService roleService;
    @Autowired
    ManagerService managerService;
    @Autowired
    ManagerRoleService managerRoleService;
    @Autowired
    PermissionService permissionService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<RolePermission> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return rolePermissionDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<RolePermission> listByRole(int rid) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return rolePermissionDAO.findAllByRid(rid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Set<String> listByManager(String username) {
        Set<String> rolePermissions = new HashSet<>();
        List<Role> roles = roleService.listByManager(managerService.getByName(username));
        for (Role role:
                roles) {
            List<RolePermission> rolePermissionList = listByRole(role.getId());
            fillRolePermission(rolePermissionList);
            for (RolePermission rolePermission:
                    rolePermissionList) {
                String name = rolePermission.getPermission().getModule().getUrl();
                rolePermissions.add(name);
            }
        }
        return rolePermissions;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Permission> listPermissionByManager(String username) {
        List<Permission> permissions = new ArrayList<>();
        Manager manager = managerService.getByName(username);
        List<ManagerRole> managerRoles = managerRoleService.listByManager(manager.getId());
        for (ManagerRole managerRole:
                managerRoles) {
            int rid = managerRole.getRid();
            List<RolePermission> rolePermissionList = listByRole(rid);
            for (RolePermission rolePermission:
                    rolePermissionList) {
                fillRolePermission(rolePermission);
                permissions.add(rolePermission.getPermission());
            }
        }
        return permissions;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<RolePermission> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = rolePermissionDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @CacheEvict(allEntries = true)
    public void add(RolePermission rolePermission)
    {
        rolePermissionDAO.save(rolePermission);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        rolePermissionDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void delete(int rid,int pid)
    {
        RolePermission rolePermission = get(rid,pid);
        rolePermissionDAO.delete(rolePermission.getId());
    }

    public void deleteByRole(int rid) {
        RolePermissionService rolePermissionService = SpringContextUtils.getBean(RolePermissionService.class);

        for (RolePermission rolePermission:
                listByRole(rid)) {
            rolePermissionService.delete(rolePermission.getId());
        }
    }

    @CacheEvict(allEntries = true)
    public void update(RolePermission rolePermission)
    {
        rolePermissionDAO.save(rolePermission);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public RolePermission get(int id)
    {
        return rolePermissionDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public RolePermission get(int rid,int pid)
    {
        return rolePermissionDAO.findByRidAndPid(rid,pid);
    }

    public void fillRolePermission(RolePermission rolePermission) {
        int rid = rolePermission.getRid();
        rolePermission.setRole(roleService.get(rid));
        int pid = rolePermission.getPid();
        Permission permission = permissionService.get(pid);
        permissionService.fill(permission);
        rolePermission.setPermission(permission);

    }

    public void fillRolePermission(List<RolePermission> rolePermissions) {
        RolePermissionService rolePermissionService = SpringContextUtils.getBean(RolePermissionService.class);
        for (RolePermission rolePermission:
                rolePermissions) {
            rolePermissionService.fillRolePermission(rolePermission);
        }
    }

}
