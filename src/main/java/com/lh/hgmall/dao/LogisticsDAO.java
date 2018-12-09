package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Logistics;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface LogisticsDAO extends JpaRepository<Logistics,Integer> {
    public List<Logistics> findAllByOidAndDateBefore(int oid, Date date, Sort sort);
}
