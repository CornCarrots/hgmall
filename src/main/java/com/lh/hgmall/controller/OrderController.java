package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Logistics;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.OrderItem;
import com.lh.hgmall.dao.OrderDAO;
import com.lh.hgmall.service.LogisticsService;
import com.lh.hgmall.service.OrderItemService;
import com.lh.hgmall.service.OrderService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    LogisticsService logisticsService;

    @GetMapping("/admin/sales/info")
    public Map<String, Object> getInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        Order order = new Order();
        orderService.fill(order);
        map.put("order", order);
        int[] orders_pay = orderService.listSalesByStatus(OrderDAO.type_waitPay);
        int[] orders_delivery = orderService.listSalesByStatus(OrderDAO.type_waitDelivery);
        int[] orders_confirm = orderService.listSalesByStatus(OrderDAO.type_waitConfirm);
        int[] orders = orderService.listSalesByStatus("");
        map.put("orders_pay", orders_pay);
        map.put("orders_delivery", orders_delivery);
        map.put("orders_confirm", orders_confirm);
        map.put("orders", orders);
        return map;
    }

    @GetMapping("/admin/sales/map")
    public List<Map<String, Object>> getMap() throws Exception {
        String[] places =
                {
                        "北京市",
                        "天津市",
                        "河北省",
                        "山西省",
                        "内蒙古自治区",
                        "辽宁省",
                        "吉林省",
                        "黑龙江省",
                        "上海市",
                        "江苏省",
                        "浙江省",
                        "安徽省",
                        "福建省",
                        "江西省",
                        "山东省",
                        "河南省",
                        "湖北省",
                        "湖南省",
                        "广东省",
                        "广西壮族自治区",
                        "海南省",
                        "重庆市",
                        "四川省",
                        "贵州省",
                        "云南省",
                        "西藏自治区",
                        "陕西省",
                        "甘肃省",
                        "青海省",
                        "宁夏回族自治区",
                        "新疆维吾尔自治区",
                        "台湾省",
                        "香港特别行政区",
                        "澳门特别行政区"
                };
        List<Map<String, Object>> list = orderService.listMap(places);
        System.out.println(list);
        return list;
    }

    @GetMapping("/admin/orders")
    public PageUtil<Order> list(HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception {
        Manager manager = (Manager) session.getAttribute("manager");
        PageUtil<Order> pageUtil = orderService.listByStatusNotAndTypeAndSid(start, size, 5, OrderDAO.type_delete,'0',manager.getSid());
        List<Order> orders = pageUtil.getContent();
        orderService.fillUser(orders);
        pageUtil.setContent(orders);
        return pageUtil;
    }

    @PutMapping("/admin/orders/{id}")
    public void delivery(@PathVariable("id") int id) throws Exception {
        Order order = orderService.get(id);
        if (order.getPayment().equals(OrderDAO.payment_delivery))
            order.setStatus(OrderDAO.type_waitPay);
        else
            order.setStatus(OrderDAO.type_waitConfirm);
        order.setDeliveryDate(new Date());
        logisticsService.init(order.getId());
        orderService.update(order);
    }

    @DeleteMapping("/admin/orders/{id}")
    public String delete(@PathVariable("id") int id) throws Exception {
        Order order = orderService.get(id);
        order.setStatus(OrderDAO.type_delete);
        orderService.update(order);
        return null;
    }

    @PostMapping("/admin/orders/search")
    public List<Order> search(@RequestParam("key") String key,HttpSession session) throws Exception {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Order> orders = orderService.listByOrderCodeAndSid(key,manager.getSid());
        orderService.fillUser(orders);
        return orders;
    }

    @GetMapping("/admin/orders/{id}")
    public Map<String, Object> get(@PathVariable("id") int id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<OrderItem> orderItems = orderItemService.listByOrder(id);
        orderItemService.fill(orderItems);
        Order order = orderService.get(id);
        orderService.fillUser(order);
        List<Logistics> logistics = logisticsService.listByOrder(id);
        map.put("orderItems", orderItems);
        map.put("order", order);
        map.put("logistics", logistics);
        return map;
    }

    @GetMapping("/admin/orders/exchange/{id}")
    public Map<String, Object> getExchange(@PathVariable("id") int id) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<OrderItem> orderItems = orderItemService.listByOrder(id);
        orderItemService.fill(orderItems);
        Order order = orderService.get(id);
        orderService.fillUser(order);
        orderService.fillExchange(order);
        map.put("orderItems", orderItems);
        map.put("order", order);
        return map;
    }

}
