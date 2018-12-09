package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Carousel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarouselDAO extends JpaRepository<Carousel,Integer> {

    public static String type_home = "type_home";

    public static String type_store = "type_store";

    public static String type_member = "type_member";

    public List<Carousel> findAllByTypeAndStatus(String type, int status, Sort sort);

    public List<Carousel> findAllByType(String type,Sort sort);

    public List<Carousel> findAllBySid(int sid,Sort sort);

    public Carousel findByRole(int role);

}
