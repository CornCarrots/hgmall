package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleDAO extends JpaRepository<Article,Integer> {
    public List<Article> findAllByStatusAndCid(int status,int cid, Sort sort);
    public List<Article> findAllByStatusNotAndTitleContaining(int status,String title, Sort sort);
    public Page<Article> findAllByStatusNot(int status, Pageable pageable);
}
