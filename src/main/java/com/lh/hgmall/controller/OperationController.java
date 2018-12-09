package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Operation;
import com.lh.hgmall.service.OperationService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OperationController {
    @Autowired
    OperationService operationService;

    @GetMapping(value = "/admin/system/operations")
    public PageUtil<Operation> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception {
        start = start < 0 ? 0 : start;
        PageUtil<Operation> page = operationService.list(start, size, 5);
        return page;
    }

    @PostMapping(value = "/admin/system/operations/search")
    public List<Operation> search(@RequestParam(value = "key") String key) throws Exception {
        List<Operation> users = operationService.listByKey(key);
        return users;
    }

    @PostMapping(value = "/admin/system/operations")
    public void add(@RequestBody Operation operation) throws Exception
    {
        operationService.add(operation);
    }

    @DeleteMapping(value = "/admin/system/operations/{id}")
    public String delete(@PathVariable("id") int id) throws Exception
    {
        operationService.delete(id);
        return null;
    }

    @PutMapping(value = "/admin/system/operations/{id}")
    public void update(@RequestBody Operation operation) throws Exception
    {
        operationService.update(operation);
    }

    @GetMapping(value = "/admin/system/operations/{id}")
    public Operation get(@PathVariable("id")int id) throws Exception
    {
        return operationService.get(id);
    }

}
