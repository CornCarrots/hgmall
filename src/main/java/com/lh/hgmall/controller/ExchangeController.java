package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Order;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.dao.OrderDAO;
import com.lh.hgmall.service.OrderService;
import com.lh.hgmall.service.UserService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class ExchangeController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;


    @GetMapping("/admin/exchanges")
    public PageUtil<Order> list(HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        PageUtil<Order> pageUtil =  orderService.listByStatusLikeAndSid(start,size,5, OrderDAO.type_waitExchange,OrderDAO.type_waitRefund,OrderDAO.type_waitRejected,manager.getSid());
        List<Order> orders = pageUtil.getContent();
        orderService.fillUser(orders);
        orderService.fillExchange(orders);
        pageUtil.setContent(orders);
        return pageUtil;
    }
    @PutMapping("/admin/exchanges/refund/{id}")
    public void refund(@PathVariable("id")int id)throws Exception
    {
        Order order = orderService.get(id);
        orderService.fillUser(order);
        order.setStatus(OrderDAO.type_fail);
        User user = order.getUser();
        user.setAccount(user.getAccount()+order.getSum());
        userService.update(user);
        orderService.update(order);
    }
    @PutMapping("/admin/exchanges/rejected/{id}")
    public void rejected(@PathVariable("id")int id)throws Exception
    {
        Order order = orderService.get(id);
        orderService.fillUser(order);
        order.setStatus(OrderDAO.type_fail);
        User user = order.getUser();
        user.setAccount(user.getAccount()+order.getSum());
        userService.update(user);
        orderService.update(order);
    }
    @PutMapping("/admin/exchanges/exchange/{id}")
    public void exchange(@PathVariable("id")int id)throws Exception
    {
        Order order = orderService.get(id);
        order.setStatus(OrderDAO.type_waitConfirm);
        order.setDeliveryDate(new Date());
        orderService.update(order);
    }
}
