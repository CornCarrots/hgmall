package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Member;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.service.MemberService;
import com.lh.hgmall.service.UserService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {
    @Autowired
    UserService userService;
    @Autowired
    MemberService memberService;

    @GetMapping(value = "/admin/members")
    public PageUtil<User> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception {
        start = start < 0 ? 0 : start;
        PageUtil<User> page = userService.listMember(start, size, 5, 0);
        List<User> users = page.getContent();
        userService.fillMember(users);
        page.setContent(users);
        return page;
    }

    @PostMapping(value = "/admin/members/search")
    public List<User> search(@RequestParam(value = "key") String key) throws Exception {
        List<User> users = userService.listByKeyAndMember(key, 0);
        userService.fillMember(users);
        return users;
    }

    @GetMapping(value = "/admin/scores")
    public PageUtil<Member> listScore(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception
    {
        start = start < 0 ? 0 : start;
        PageUtil<Member> page = memberService.list(start, size, 5);
        return page;
    }

    @PostMapping(value = "/admin/scores")
    public void add(@RequestBody Member member) throws Exception
    {
        memberService.add(member);
    }

    @DeleteMapping(value = "/admin/scores/{id}")
    public String delete(@PathVariable("id") int id) throws Exception
    {
        memberService.delete(id);
        return null;
    }

    @PutMapping(value = "/admin/scores/{id}")
    public void update(@RequestBody Member member) throws Exception
    {
        memberService.update(member);
    }

    @GetMapping(value = "/admin/scores/{id}")
    public Member get(@PathVariable("id")int id) throws Exception
    {
        Member member =  memberService.get(id);
        return member;
    }

    @PostMapping(value = "/admin/scores/search")
    public List<Member> searchScore(@RequestParam(value = "key") String key) throws Exception {
        List<Member> members = memberService.listByKey(key);
        return members;
    }


}
