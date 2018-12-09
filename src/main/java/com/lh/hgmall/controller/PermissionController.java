package com.lh.hgmall.controller;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.service.*;
import com.lh.hgmall.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PermissionController {
    @Autowired
    PermissionService permissionService;
    @Autowired
    ModuleService moduleService;
    @Autowired
    OperationService operationService;
    @Autowired
    RoleService roleService;
    @Autowired
    RolePermissionService rolePermissionService;

    @GetMapping(value = "/admin/roles/{rid}/permissions/operations")
    public List<Operation> listOperation(){
        return operationService.list();
    }

    @PostMapping(value = "/admin/roles/{rid}/permissions/tree")
    public Object tree(@PathVariable("rid")int rid) throws Exception{
        Role role = roleService.get(rid);
        roleService.fill(role);
        List<Map<String, Object>> modules = moduleService.listForTree(role);
        List<Map<String, Object>> resultList = TreeUtil.treeViewDataTransform2(modules);
        return resultList;
    }

    @GetMapping(value = "/admin/roles/{rid}/permissions/{mid}")
    public List<Operation> listOperation(@PathVariable("rid")int rid,@PathVariable("mid")int mid){
        Role role = roleService.get(rid);
        roleService.fill(role);
        Module module = moduleService.get(mid);
        moduleService.fill(module,role.getPermissions());
        return module.getOperations();
    }

    @PutMapping(value = "/admin/roles/{rid}/permissions/{mid}")
    public void updatePermission(@PathVariable("rid")int rid,@RequestBody Permission permission){
        int pid = 0;
        Permission permission1 = permissionService.get(permission.getOid(),permission.getMid());
        System.out.println(permission1);
        if(permission1==null)
        {
            permissionService.add(permission);
             pid = permission.getId();
        }
        else
            pid = permission1.getId();
        RolePermission rolePermission = new RolePermission();
        rolePermission.setPid(pid);
        rolePermission.setRid(rid);
        System.out.println(rolePermission);
        rolePermissionService.add(rolePermission);
    }

    @DeleteMapping(value = "/admin/roles/{rid}/permissions/{mid}/{oid}")
    public void deletePermission(@PathVariable("rid")int rid,@PathVariable("oid")int oid,@PathVariable("mid")int mid){
        Permission permission1 = permissionService.get(oid,mid);
        rolePermissionService.delete(rid,permission1.getId());
    }

}
