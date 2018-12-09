package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberDAO extends JpaRepository<Member,Integer> {
    public List<Member> findAllByNameContaining(String key, Sort sort);
}
