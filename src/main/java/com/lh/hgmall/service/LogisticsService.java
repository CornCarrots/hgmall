package com.lh.hgmall.service;

import com.lh.hgmall.bean.Logistics;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.dao.LogisticsDAO;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@CacheConfig(cacheNames = "logistics")
public class LogisticsService {
    @Autowired
    LogisticsDAO logisticsDAO;
    @Autowired
    OrderService orderService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Logistics> listByOrder(int oid)
    {
        Sort sort = new Sort(Sort.Direction.DESC,"date");
        return logisticsDAO.findAllByOidAndDateBefore(oid,new Date(),sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Logistics logistics){
        logisticsDAO.save(logistics);
    }

    public void init(int oid){

        LogisticsService logisticsService = SpringContextUtils.getBean(LogisticsService.class);

        Order order = orderService.get(oid);
        String city = order.getAddress().split("-")[1];
        String dist = order.getAddress().split("-")[2];
        String address = order.getAddress().split("-")[3];

        Date today1 = Calendar.getInstance().getTime();
        Logistics logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(today1);
        logistics.setLocation("您的订单待配货");
        logisticsService.add(logistics);

        int random1 = (int)(Math.random()*(24-today1.getHours()));
        int random2 = (int)(Math.random()*60);
        int random3 = (int)(Math.random()*60);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today1);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==today1.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-today1.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==today1.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-today1.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date today2 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(today2);
        logistics.setLocation("包裹正在等待揽收");
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*(24-today2.getHours()));
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(today2);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==today2.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-today2.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==today2.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-today2.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date today3 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(today3);
        logistics.setLocation("【"+city+"】快件已到达"+dist+"集货点");
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*24);
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,random1);
        calendar.set(Calendar.MINUTE,random2);
        calendar.set(Calendar.SECOND,random3);
        Date tomorrow1 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(tomorrow1);
        logistics.setLocation("【"+city+"】快件已从"+dist+"集货点发出，准备发往"+city+"转运中心");
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*(24-tomorrow1.getHours()));
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(tomorrow1);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==tomorrow1.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-tomorrow1.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==tomorrow1.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-tomorrow1.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date tomorrow2 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(tomorrow2);
        logistics.setLocation("【"+city+"】快件已到达"+city+"转运中心");
        logisticsService.add(logistics);


        random1 = (int)(Math.random()*(24-tomorrow2.getHours()));
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(tomorrow2);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==tomorrow2.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-tomorrow2.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==tomorrow2.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-tomorrow2.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date tomorrow3 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(tomorrow3);
        logistics.setLocation("【"+city+"】快件已从"+city+"转运中心发出,准备发往"+city+dist+"分部");
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*24);
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,2);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        calendar.set(Calendar.MINUTE,random2);
        calendar.set(Calendar.SECOND,random3);
        Date afterTomorrow1 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(afterTomorrow1);
        logistics.setLocation("【"+city+"】快件已到达"+city+dist+"分部分拨中心进行卸车扫描");
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*(24-afterTomorrow1.getHours()));
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(afterTomorrow1);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==afterTomorrow1.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-afterTomorrow1.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==afterTomorrow1.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-afterTomorrow1.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date afterTomorrow2 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(afterTomorrow2);
        logistics.setLocation("【"+city+"】快件从"+city+dist+"分部分拨中心发出，准备发往"+address);
        logisticsService.add(logistics);

        random1 = (int)(Math.random()*(24-afterTomorrow2.getHours()));
        random2 = (int)(Math.random()*60);
        random3 = (int)(Math.random()*60);
        calendar = Calendar.getInstance();
        calendar.setTime(afterTomorrow2);
        calendar.add(Calendar.HOUR_OF_DAY,random1);
        if(calendar.getTime().getHours()==afterTomorrow2.getHours())
            calendar.set(Calendar.MINUTE,(int)(Math.random()*(60-afterTomorrow2.getMinutes())));
        else
            calendar.set(Calendar.MINUTE,random2);
        if(calendar.getTime().getMinutes()==afterTomorrow2.getMinutes())
            calendar.set(Calendar.SECOND,(int)(Math.random()*(60-afterTomorrow2.getMinutes())));
        else
            calendar.set(Calendar.SECOND,random3);
        Date afterTomorrow3 = calendar.getTime();
        logistics = new Logistics();
        logistics.setOid(oid);
        logistics.setDate(afterTomorrow3);
        logistics.setLocation("【"+city+"】"+address+"派件员正在为您派件");
        logisticsService.add(logistics);

    }
}
