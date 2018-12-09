package com.lh.hgmall.service;

import com.lh.hgmall.bean.Product;
import com.lh.hgmall.bean.Review;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.dao.ReviewDAO;
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
@CacheConfig(cacheNames = "review")
public class ReviewService {
    @Autowired
    ReviewDAO reviewDAO;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Review> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return reviewDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Review> list(int start, int size, int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = reviewDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page, number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Review> listByProduct(int pid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return reviewDAO.findAllByPid(pid,sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Review review)
    {
        reviewDAO.save(review);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        reviewDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Review review)
    {
        reviewDAO.save(review);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Review get(int id)
    {
        return reviewDAO.findOne(id);
    }

    public void fill(Review review)
    {
        int uid = review.getUid();
        User user = userService.get(uid);
        review.setUser(user);
        int pid = review.getPid();
        Product product = productService.get(pid);
        review.setProduct(product);
    }
    public void fill(List<Review> reviews){
        ReviewService reviewService = SpringContextUtils.getBean(ReviewService.class);
        for (Review review:
             reviews) {
            reviewService.fill(review);
        }
    }
}
