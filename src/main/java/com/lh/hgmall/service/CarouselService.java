package com.lh.hgmall.service;

import com.lh.hgmall.bean.Carousel;
import com.lh.hgmall.dao.CarouselDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "carousel")
public class CarouselService {
    @Autowired
    CarouselDAO carouselDAO;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Carousel> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "role");
        return carouselDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Carousel> listByType(String type)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "role");
        return carouselDAO.findAllByType(type,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Carousel> listBySid(int sid)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "role");
        return carouselDAO.findAllBySid(sid,sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Carousel carousel) {
        int role = listByType(carousel.getType()).size();
        carousel.setRole(role+1);
        carouselDAO.save(carousel);
    }

    @CacheEvict(allEntries = true)
    public void update(Carousel carousel) {
        Carousel carousel1 = get(carousel.getId());
        System.out.println(carousel1);
        System.out.println(carousel1);
        int role1 =carousel1.getRole();
        if (role1==carousel.getRole())
        {
            carouselDAO.save(carousel);
            return;
        }
        else
        {
            Carousel carousel2 = getByRole(carousel.getRole());
            carousel2.setRole(role1);
            System.out.println(carousel2);
            carouselDAO.save(carousel);
        }

    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        Carousel carousel = get(id);
        int role = carousel.getRole();
        for (Carousel carousel1:
             listByType(carousel.getType())) {
            int role1 = carousel1.getRole();
            if (role1>role)
                carousel1.setRole(role1-1);
        }
        carouselDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Carousel get(int id)
    {
        return carouselDAO.findOne(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Carousel getByRole(int role){return carouselDAO.findByRole(role);}
}
