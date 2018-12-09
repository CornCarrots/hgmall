package com.lh.hgmall.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.service.UserService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/admin/users")
    public PageUtil<User> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        start = start<0?0:start;
        PageUtil<User> page =userService.list(start,size,5);
        List<User> users = page.getContent();
        userService.fillOrder(users);
        page.setContent(users);
        return page;
    }

    @PutMapping(value = "/admin/users/shield/{id}")
    public void shieldUser(@PathVariable("id")int id)throws Exception
    {
        User user = userService.get(id);
        user.setStatus(1);
        userService.update(user);
    }
    @PutMapping(value = "/admin/users/check/{id}")
    public void checkUser(@PathVariable("id")int id)throws Exception
    {
        User user = userService.get(id);
        user.setStatus(0);
        userService.update(user);
    }

    @PostMapping(value = "/admin/users/search")
    public List<User> search(@RequestParam(value = "key") String key) throws Exception
    {
        List<User> users =  userService.listByKey(key);
        userService.fillOrder(users);
        return users;
    }

    @GetMapping(value = "/admin/users/{id}")
    public User getUser(@PathVariable("id")int id)throws Exception
    {
        User user = userService.get(id);
        userService.fillOrder(user);
        return user;
    }



}
