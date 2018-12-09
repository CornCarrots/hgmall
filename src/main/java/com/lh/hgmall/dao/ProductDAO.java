package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product,Integer> {
    public List<Product> findAllByCidAndStatus(int cid,int status, Sort sort);
    public List<Product> findAllByStatus(int status, Sort sort);
    public List<Product> findAllByStatusAndNameContaining(int status,String name, Sort sort);
    public List<Product> findAllByCidAndStatusAndNameContaining(int cid,int status,String name, Sort sort);
    public Page<Product> findAllByCidAndStatus(int cid, int status,Pageable pageable);
}
