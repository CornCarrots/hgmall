package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PropertyDAO extends JpaRepository<Property,Integer> {

    public Page<Property> findAllByCid(int cid, Pageable pageable);

    public List<Property> findAllByCid(int cid, Sort sort);

    public List<Property> findAllByCidAndNameContaining(int cid, String name, Sort sort);

}
