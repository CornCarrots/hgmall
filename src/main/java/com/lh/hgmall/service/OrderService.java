package com.lh.hgmall.service;

import com.lh.hgmall.bean.Exchange;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.OrderItem;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.dao.OrderDAO;
import com.lh.hgmall.util.CalendarRandomUtil;
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

import java.util.*;

@Service
@CacheConfig(cacheNames = "order")
public class OrderService {
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    OrderItemService orderItemService;

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return orderDAO.findAll(sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Order> list(int start,int size,int number) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Order> page = orderDAO.findAll(pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByUser(int uid)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return orderDAO.findAllByUid(uid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByUserAndType(int uid,char type)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return orderDAO.findAllByUidAndType(uid,type,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByStatus(String status) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        return orderDAO.findAllByStatus(status,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByUserAndStatusLike(int uid,String status1,String status2,String status3) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return orderDAO.findAllByUidAndStatusInOrStatusInOrStatusIn(uid,status1,status2,status3);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Order> listByStatusLikeAndSid(int start,int size,int number,String status1,String status2,String status3,int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Order> page = orderDAO.findAllByStatusInOrStatusInOrStatusInAndSid(pageable,status1, status2, status3,sid);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Order> listByStatusNotAndTypeAndSid(int start,int size,int number,String status,char type,int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Order> page = null;
        if(sid ==0)
             page = orderDAO.findAllByStatusNotAndType(pageable,status,type);
        else
            page = orderDAO.findAllByStatusNotAndTypeAndSid(pageable,status,type,sid);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByUserAndStatusNotAndType(int uid , String status,char type) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return orderDAO.findAllByUidAndStatusNotAndType(uid,status,type,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Order> listByUserAndStatusAndType(int uid , String status,char type) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return orderDAO.findAllByUidAndStatusAndType(uid,status,type,sort);
    }

    public List<Order> listByOrderCodeAndSid(String code,int sid)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if(sid == 0 )
            return orderDAO.findAllByOrderCodeContaining(code,sort);
        else
            return orderDAO.findAllByOrderCodeContainingAndSid(code,sid,sort);
    }

    @CacheEvict(allEntries = true)
    public void recharge(float money,User user){
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        Order order = new Order();
        order.setOrderCode(CalendarRandomUtil.getRandom() );
        order.setAddress("");order.setPost("");order.setReceiver("");order.setMobile("");order.setUserMessage("");order.setExpress("");order.setPayment("");
        order.setCreateDate(new Date());
        order.setPayDate(new Date());
        order.setDeliveryDate(new Date());
        order.setConfirmDate(new Date());
        order.setUid(user.getId());
        order.setStatus(OrderDAO.type_success);
        order.setSum( money);
        order.setQuantity(1);
        order.setType('1');
        order.setSid(0);
        orderService.add(order);
        OrderItem item = new OrderItem();
        item.setUid(user.getId());
        item.setNumber(1);
        item.setPid(0);
        item.setSid(0);
        item.setOid(order.getId());
        orderItemService.add(item);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public int[] listSalesByStatus(String status)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        int[] arr = new int[12];
        List<Order> orders = null;
        if(status.equals(""))
             orders = list();
        else
         orders = orderService.listByStatus(status);
        for (int i = 1; i <13 ; i++) {
            int sum = 0;
            for (Order order:
                    orders) {
                if (order.getCreateDate().getMonth()+1==i)
                    sum++;
            }
            arr[i-1]=sum;
        }
        return arr;
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Map<String,Object>> listMap(String[] place)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < place.length; i++) {
            Map<String,Object> map = new HashMap<>();
            if(place[i].endsWith("省"))
                place[i] = place[i].substring(0,place[i].indexOf("省"));
            else if(place[i].endsWith("市"))
                place[i] = place[i].substring(0,place[i].indexOf("市"));
            else if(place[i].endsWith("特别行政区"))
                place[i] = place[i].substring(0,place[i].indexOf("特别行政区"));
            else if(place[i].endsWith("自治区"))
            {
                place[i] = place[i].substring(0,place[i].indexOf("自治区"));
                if(place[i].startsWith("内蒙古"))
                    place[i] = "内蒙古";
                if(place[i].startsWith("广西"))
                    place[i] = "广西";
                if(place[i].startsWith("西藏"))
                    place[i] = "西藏";
                if(place[i].startsWith("宁夏"))
                    place[i] = "宁夏";
                if(place[i].startsWith("新疆"))
                    place[i] = "新疆";
            }
            map.put("name",place[i]);
            int sum = 0;
            for (Order order:orderService.list()) {
                if(order.getAddress().split("-")[0].startsWith(place[i]))
                    sum++;
            }
            map.put("value",sum);
            list.add(map);
        }
        return list;
    }

    @CacheEvict(allEntries = true)
    public void add(Order order) {
        orderDAO.save(order);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        orderDAO.delete(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Order order) {
        orderDAO.save(order);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Order get(int id) {
        return orderDAO.findOne(id);
    }

    public void fill(Order order) {
        List<Order> success = listByStatus(OrderDAO.type_success);
        List<Order> fail = listByStatus(OrderDAO.type_fail);
        if (success == null)
            order.setTotalSuccess(0);
        else
            order.setTotalSuccess(success.size());
        if (fail == null)
            order.setTotalFail(0);
        else
            order.setTotalFail(fail.size());
        int sum = 0;
        int quantity = 0;
        List<Order> orders = list();
        if (orders != null)
            for (Order order1 :
                    orders) {
                sum += order1.getSum();
                quantity += order1.getQuantity();
            }
        order.setTotalSum(sum);
        order.setTotalQuantity(quantity);
    }

    public void fillUser(Order order)
    {
        int uid = order.getUid();
        order.setUser(userService.get(uid));
    }

    public void fillUser(List<Order> orders)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);
        for (Order order:
             orders) {
            orderService.fillUser(order);
        }
    }

    public void fillStore(Order order)
    {
        int sid = order.getSid();
        order.setStore(storeService.get(sid));

    }

    public void fillStore(List<Order> orders)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        for (Order order:
             orders) {
            orderService.fillStore(order);
        }
    }

    public void fillOrderItem(Order order)
    {
        List<OrderItem> items = new ArrayList<>();
        items = orderItemService.listByOrder(order.getId());
        orderItemService.fill(items);
        order.setOrderItems(items);
    }

    public void fillOrderItem(List<Order> orders)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        for (Order order:
             orders) {
            orderService.fillOrderItem(order);
        }
    }
    public void fillExchange(Order order)
    {
        Exchange exchange = exchangeService.getByOrder(order.getId());
        order.setExchange(exchange);
    }

    public void fillExchange(List<Order> orders)
    {
        OrderService orderService = SpringContextUtils.getBean(OrderService.class);

        for (Order order:
             orders) {
            orderService.fillExchange(order);
        }
    }

}
