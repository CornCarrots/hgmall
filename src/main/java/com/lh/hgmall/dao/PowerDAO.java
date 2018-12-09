package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Power;
import com.lh.hgmall.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PowerDAO extends JpaRepository<Power,Integer> {
    public List<Power> findAllByMid(int mid, Sort sort);
    public List<Power> findAllByMidNot(int mid, Sort sort);
    public List<Power> findAllByMidAndAndTitleContaining(int mid,String title, Sort sort);
    public Page<Power> findAllByMid(int mid, Pageable pageable);
}
