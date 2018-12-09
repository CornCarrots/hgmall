package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Store;
import com.lh.hgmall.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreDAO extends JpaRepository<Store,Integer> {
    public static String type_private = "type_private";
    public static String type_business = "type_business";

    Page<Store> findByStatusNot(int status, Pageable pageable);
    List<Store> findByStatusNot(int status, Sort sort);
    List<Store> findByStatusNotAndCid(int status,int cid, Sort sort);
    List<Store> findByStatusNotAndNameContaining(int status, String name, Sort sort);
}
