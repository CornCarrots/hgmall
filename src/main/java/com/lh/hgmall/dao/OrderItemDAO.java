package com.lh.hgmall.dao;

import com.lh.hgmall.bean.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {

    public List<OrderItem> findAllByOid(int oid, Sort sort);


    public List<OrderItem> findAllByUidAndOid(int uid,int oid, Sort sort);

}
