package com.lh.hgmall.dao;

import com.lh.hgmall.bean.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageDAO extends JpaRepository<ProductImage,Integer> {
    public static String type_single = "type_single";
    public static String type_detail = "type_detail";

    public List<ProductImage> findAllByPid(int pid, Sort sort);
    public List<ProductImage> findAllByPidAndType(int pid,String type, Sort sort);
}
