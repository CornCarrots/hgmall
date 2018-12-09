package com.lh.hgmall.dao;

import com.lh.hgmall.bean.ManagerRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRoleDAO extends JpaRepository<ManagerRole,Integer> {
    public List<ManagerRole> findAllByMid(int mid, Sort sort);
    public List<ManagerRole> findAllByRid(int rid, Sort sort);
    public ManagerRole findByRidAndMid(int rid,int mid);
}
