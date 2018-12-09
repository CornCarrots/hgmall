package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Advertisement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementDAO extends JpaRepository<Advertisement,Integer> {
    public static String type_home = "type_home";

    public static String type_category = "type_category";

    public static String type_login = "type_login";

    public static String type_loginStore = "type_loginStore";

    public List<Advertisement> findAllByType(String type, Sort sort);

}
