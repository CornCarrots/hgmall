package com.lh.hgmall.service;

import com.lh.hgmall.bean.Module;
import com.lh.hgmall.bean.Operation;
import com.lh.hgmall.bean.Permission;
import com.lh.hgmall.bean.Role;
import com.lh.hgmall.dao.ModuleDAO;
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
@CacheConfig(cacheNames = "module")
public class ModuleService {
    @Autowired
    ModuleDAO moduleDAO;
    @Autowired
    PermissionService permissionService;
    @Autowired
    OperationService operationService;

    Sort sort = new Sort(Sort.Direction.DESC,"id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Module> list()
    {
        return moduleDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Module> list(int start,int size,int number)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Module> page = moduleDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    @CacheEvict(allEntries = true)
    public void add(Module module)
    {
        moduleDAO.save(module);
    }

    @CacheEvict(allEntries = true)
    public void update(Module module){
        moduleDAO.save(module);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        moduleDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Module get(int id){
        return moduleDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Module> getByURL(String url){
        return moduleDAO.findAllByUrl(url);
    }

    public void fill(Module module,List<Permission> permissions){
        Map<Module,List<Operation>> map = new HashMap<>();
        for (Permission permission:
                permissions) {
            if (permission.getMid()!=module.getId())
                continue;
            Operation operation = operationService.get(permission.getOid());
            if(map.containsKey(module))
            {
                map.get(module).add(operation);
            }
            else
            {
                List<Operation> operations = new ArrayList<>();
                operations.add(operation);
                map.put(module,operations);
            }
        }
        module.setOperations(map.get(module));
    }

    public void fill(List<Module> modules,List<Permission> permissions){
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);
        for (Module module:
             modules) {
            moduleService.fill(module,permissions);
        }
    }

    public List<Module> listByKey(String key)
    {
        return moduleDAO.findAllByNameContaining(key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Map<String, Object>> listForTree(Role role) {
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        List<Module> modules = list();
        List<Module> modules1 = new ArrayList<>();
        for (Permission permission:
                role.getPermissions()) {
            Module module = moduleService.get(permission.getMid());
            if(!modules1.contains(module))
                modules1.add(module);
        }
        List<Map<String, Object>> list = new ArrayList<>();
        int i = 0;
        int pid = -1;
        boolean found = false;
        boolean check = false;
        for (Module module :
                modules) {
            Map<String, Object> map = new HashMap<>();
            map.put("parent_id", module.getPid());
            map.put("layer_name", module.getDesc());
            map.put("id", module.getId());
            Map<String, Object> state = new HashMap<>();
            if(modules1.size()==0)
            {
                if (module.getPid()==0)
                {
                    state.put("checked", false);
                    state.put("selected",true);
                    state.put("expanded",true);
                }
            }
            else
            {
                for (Module module1:
                        modules1) {
                    if(module1.getId()==module.getId())
                    {
                        if(!found)
                        {
                            state.put("selected",true);
                            pid = module1.getPid();
                            found = true;
                        }
                        else
                        {
                            if(module1.getId()==pid)
                            {
                                state.put("selected",false);
//                            state.put("expanded",true);
                                pid = module1.getPid();
                            }
                            else
                            {
                                state.put("selected",false);
//                            state.put("expanded",false);
                            }
                        }
                        state.put("expanded",true);
                        state.put("checked",true);
                        check = true;
                        break;
                    }

                }
                if(!check)
                {
                    state.put("checked",false);
                    state.put("selected",false);
                    state.put("expanded",false);
                }
            }
            map.put("state",state);
            list.add(map);
            i++;
        }
        return list;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator", unless = "#result.empty")
    public List<Module> listRoot() {

        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        return moduleService.listByParent(0);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Module> listByParent(int pid) {
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        return moduleDAO.findAllByPid(pid,sort);
    }


    public boolean hasChild(Module module) {
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        int mid = module.getId();
        return moduleService.listByParent(mid).size() != 0;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<String> listURL() {
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        List<String> urls = new ArrayList<>();
        for (Module module :
                moduleService.listRoot()) {
            String url = "";
            url += module.getUrl();
            if (hasChild(module)) {
                urls.addAll(moduleService.listURLByParent(url,module.getId()));
            }
            else
            {
                urls.add(url);
            }
        }
        return urls;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<String> listURLByParent(String s, int pid) {
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        List<String> urls = new ArrayList<>();
        for (Module module :
                moduleService.listByParent(pid)) {
            s += module.getUrl();
            if (hasChild(module))
                urls.addAll(moduleService.listURLByParent(s, module.getId()));
            else
                urls.add(s);
            s=s.substring(0,s.lastIndexOf("/"));
        }
        return urls;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public String getChildURL(String url) {
        ModuleService moduleService = SpringContextUtils.getBean(ModuleService.class);

        for (String url1:
                moduleService.listURL()) {
            if(url1.contains(url))
            {
                if(url1.endsWith(url))
                    return url1;
            }
        }
        return null;
    }
}
