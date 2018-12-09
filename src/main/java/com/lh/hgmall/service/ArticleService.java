package com.lh.hgmall.service;

import com.lh.hgmall.bean.Article;
import com.lh.hgmall.dao.ArticleDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "article")
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    CategoryService categoryService;

    Sort sort = new Sort(Sort.Direction.DESC,"id");

    public List<Article> listBySuccessAndCategory(int status,int cid)
    {
        return articleDAO.findAllByStatusAndCid(status,cid,sort);
    }

    public List<Article> listByStatusAndKey(int status,String key)
    {
        return articleDAO.findAllByStatusNotAndTitleContaining(status,key,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Article> listByStatus(int start, int size, int number, int status)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page page = articleDAO.findAllByStatusNot(status,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<Article>(page,number);
    }

    @CacheEvict(allEntries = true)
    public void add(Article article){
        articleDAO.save(article);
    }

    @CacheEvict(allEntries = true)
    public void update(Article article){
        articleDAO.save(article);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        articleDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Article get(int id){
        return articleDAO.findOne(id);
    }

    public void fillCategory(Article article)
    {
        int cid = article.getCid();
        article.setCategory(categoryService.get(cid));
    }

    public void fillCategory(List<Article> articles)
    {
        ArticleService articleService = SpringContextUtils.getBean(ArticleService.class);
        for (Article article:
             articles) {
            articleService.fillCategory(article);
        }
    }
}
