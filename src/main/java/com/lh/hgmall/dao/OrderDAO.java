package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order,Integer> {
    public static String type_waitPay = "type_waitPay";
    public static String type_waitDelivery = "type_waitDelivery";
    public static String type_waitConfirm = "type_waitConfirm";
    public static String type_waitReview = "type_waitReview";
    public static String type_success = "type_success";
    public static String type_waitExchange = "type_waitExchange";
    public static String type_waitRefund = "type_waitRefund";
    public static String type_waitRejected = "type_waitRejected";
    public static String type_fail = "type_fail";
    public static String type_delete = "type_delete";

    public static String payment_online = "payment_online";
    public static String payment_delivery = "payment_delivery";
    public static String payment_receiving= "payment_receiving";

    public static String express_zhongtong = "express_zhongtong";
    public static String express_yuantong = "express_yuantong";
    public static String express_shunfeng = "express_shunfeng";
    public static String express_yunda = "express_yunda";
    public static String express_tiantian = "express_tiantian";

    public List<Order> findAllByStatus(String status,Sort sort);
    public List<Order> findAllByUid(int uid,Sort sort);
    public List<Order> findAllByUidAndType(int uid,char type,Sort sort);
    public List<Order> findAllByOrderCodeContaining(String orderCode, Sort sort);
    public List<Order> findAllByOrderCodeContainingAndSid(String orderCode,int sid ,Sort sort);
    public Page<Order> findAllByStatusInOrStatusInOrStatusInAndSid(Pageable pageable,String status1,String status2,String status3,int sid);
    public List<Order> findAllByUidAndStatusInOrStatusInOrStatusIn(int uid,String status1,String status2,String status3);
    public Page<Order> findAllByStatusNotAndType(Pageable pageable,String status,char type);
    public Page<Order> findAllByStatusNotAndTypeAndSid(Pageable pageable,String status,char type,int sid);
    public List<Order> findAllByUidAndStatusNotAndType(int uid,String status,char type, Sort sort);
    public List<Order> findAllByUidAndStatusAndType(int uid,String status,char type ,Sort sort);
}
