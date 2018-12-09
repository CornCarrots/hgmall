package com.lh.hgmall.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Store;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.service.ManagerService;
import com.lh.hgmall.service.StoreService;
import com.lh.hgmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ValidateController {
    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @Autowired
    ManagerService managerService;

//    @GetMapping(value = "/validate/user")
//    public String validateUser(@RequestParam("fieldId")String id, @RequestParam("fieldValue")String value){
//
//        JSONArray array = new JSONArray();
//        boolean result = userService.hasExits(value);
//        array.add(0,id);
//        array.add(1,!result);
//        array.add(2,!result?"可以使用":"该用户名已存在");
//        return array.toJSONString();
//    }

    @GetMapping(value = "/validate/userName")
    public String validateUser(@RequestParam("name")String name){
         User user = userService.getByName(name);
        if(user!=null)
            return "true";
        return "false";
    }

    @GetMapping(value = "/validate/userEmail")
    public String validateUserEmail(@RequestParam("email")String name){
         User user = userService.getByName(name);
        if(user!=null)
            return "true";
        return "false";
    }

    @GetMapping(value = "/validate/storeName")
    public String validateStoreName(@RequestParam("name")String name){
        boolean result = storeService.hasExits(name);
        if(result)
            return "true";
        return "false";
    }

    @GetMapping(value = "/validate/storeEmail")
    public String validateStoreEmail(@RequestParam("email")String email){
        Store result = storeService.hasExitsEmail(email);
        if(result!=null)
            return "true";
        return "false";
    }

//    @GetMapping(value = "/validate/manager")
//    public String validateManager(@RequestParam("managerName")String name){
//        Manager result = managerService.getByName(name);
//        if(result!=null)
//            return "true";
//        return "false";
//    }
//
//    @GetMapping(value = "/validate/manager")
//    public String validateManagerEmail(@RequestParam("email")String email){
//        Manager result = managerService.getByEmail(email);
//        if(result!=null)
//            return "true";
//        return "false";
//    }

}
