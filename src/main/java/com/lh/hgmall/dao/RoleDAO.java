package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleDAO extends JpaRepository<Role,Integer> {
    public List<Role> findAllByNameContaining(String key, Sort sort);

}
