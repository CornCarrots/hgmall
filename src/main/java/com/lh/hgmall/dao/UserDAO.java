package com.lh.hgmall.dao;

import com.lh.hgmall.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    public List<User> findAllByNameContaining(String name, Sort sort);

    public User findAllByName(String name, Sort sort);

    public List<User> findAllByNameContainingAndMidNot(String name,int mid, Sort sort);

    public List<User> findAllByMidNot(int mid, Sort sort);

    public Page<User> findAllByMidNot(int mid, Pageable pageable);

}
