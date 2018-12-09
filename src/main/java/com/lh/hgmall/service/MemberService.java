package com.lh.hgmall.service;

import com.lh.hgmall.bean.Member;
import com.lh.hgmall.dao.MemberDAO;
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

@Service
@CacheConfig(cacheNames = "member")
public class MemberService {
    @Autowired
    MemberDAO memberDAO;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Member> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return memberDAO.findAll(sort);
    }

    public List<Member> listByKey(String key) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return memberDAO.findAllByNameContaining(key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Member> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = memberDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @CacheEvict(allEntries = true)
    public void add(Member member)
    {
        memberDAO.save(member);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        memberDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Member member)
    {
        memberDAO.save(member);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Member get(int id)
    {
        return memberDAO.findOne(id);
    }
}
