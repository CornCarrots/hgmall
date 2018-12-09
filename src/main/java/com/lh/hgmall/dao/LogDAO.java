package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogDAO extends JpaRepository<Log,Integer> {
    public List<Log> findAllByUid(int uid, Sort sort);
}
