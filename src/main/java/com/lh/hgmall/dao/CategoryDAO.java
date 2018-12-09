package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryDAO extends JpaRepository<Category,Integer> {
    public final static String type_product = "type_product";
    public final static String type_article = "type_article";
    List<Category> findAllByType(String type, Sort sort);
    Page<Category> findAllByTypeAndSid(String type,int sid, Pageable pageable);
    List<Category> findAllByTypeAndNameContainingAndSid(String type,String name,int sid,Sort sort);
    List<Category> findAllByTypeAndSid(String type,int sid,Sort sort);
}
