package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeDAO extends JpaRepository<Exchange,Integer> {
    public Exchange findByOid(int oid);
}
