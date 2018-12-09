package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review,Integer> {
    public List<Review> findAllByPid(int pid, Sort sort);
}
