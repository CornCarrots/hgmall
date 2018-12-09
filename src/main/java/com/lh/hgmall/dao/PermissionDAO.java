package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Permission;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionDAO extends JpaRepository<Permission,Integer> {

    public Permission findByOidAndMid(int oid,int mid);
}
