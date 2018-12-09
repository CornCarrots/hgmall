package com.lh.hgmall.dao;

import com.lh.hgmall.bean.PropertyValue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {

    public PropertyValue findPropertyValueByPidAndPtid(int pid, int ptid);

    public List<PropertyValue> findAllByPid(int pid,Sort sort);

    public List<PropertyValue> findAllByPtid(int ptid,Sort sort);
}
