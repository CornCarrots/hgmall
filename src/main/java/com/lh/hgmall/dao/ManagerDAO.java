package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Manager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerDAO extends JpaRepository<Manager,Integer> {

    public List<Manager> findAllByNameContaining(String key, Sort sort);

    public List<Manager> findAllBySid(int sid, Sort sort);

    public List<Manager> findAllByName(String name, Sort sort);

}
