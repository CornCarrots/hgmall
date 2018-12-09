package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Message;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.dao.MessageDAO;
import com.lh.hgmall.dao.OrderDAO;
import com.lh.hgmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BackController {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    StoreService storeService;

    @GetMapping(value = "/admin/home")
    public Map<String,Object> get(HttpSession session){
        Manager manager = (Manager) session.getAttribute("manager");
        Map<String,Object> map = new HashMap<>();

        int users_num = userService.list().size();
        int stores_num = storeService.listByStatus(3).size();
        List<Order> orders = orderService.list();
        int orders_num = orders.size();
        float orders_sum = 0;
        for (Order order: orders) { orders_sum+=order.getSum(); }
        map.put("users_num",users_num);
        map.put("stores_num",stores_num);
        map.put("orders_num",orders_num);
        map.put("orders_sum",orders_sum);

        int orders_pay = orderService.listByStatus(OrderDAO.type_waitPay).size();
        int orders_delivery = orderService.listByStatus(OrderDAO.type_waitDelivery).size();
        int orders_confirm = orderService.listByStatus(OrderDAO.type_waitConfirm).size();
        int orders_success = orderService.listByStatus(OrderDAO.type_success).size();
        int orders_fail = orderService.listByStatus(OrderDAO.type_fail).size();
        map.put("orders_pay",orders_pay);
        map.put("orders_delivery",orders_delivery);
        map.put("orders_confirm",orders_confirm);
        map.put("orders_success",orders_success);
        map.put("orders_fail",orders_fail);

        int products_success = productService.listByStatus(0).size();
        int products_fail = productService.listByStatus(1).size();
        int products_all = productService.list().size();
        int category_num = categoryService.list().size();
        int review_num = reviewService.list().size();
        map.put("products_success",products_success);
        map.put("products_fail",products_fail);
        map.put("products_all",products_all);
        map.put("category_num",category_num);
        map.put("review_num",review_num);

        List<Message> messages = messageService.listMessage(MessageDAO.type_message,manager.getSid());
        if(messages.size()>5) messages = messages.subList(0,5);
        map.put("messages",messages);
        return map;
    }
}
