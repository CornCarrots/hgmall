package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Operation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationDAO extends JpaRepository<Operation,Integer> {
    public List<Operation> findAllByNameContaining(String key, Sort sort);

}
