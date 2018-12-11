package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "order_")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String orderCode;

    private String address;

    private String post;

    private String receiver;

    private String mobile;

    private String userMessage;

    private Date createDate;

    private Date payDate;

    private Date deliveryDate;

    private Date confirmDate;

    private int uid;

    private String express;

    private String status;

    private float sum;

    private int quantity;

    private String payment;

    private char type;

    private int sid;

    @Transient
    private int totalSum;

    @Transient
    private int totalQuantity;

    @Transient
    private int totalSuccess;

    @Transient
    private int totalFail;

    @Transient
    private User user;

    @Transient
    private Exchange exchange;

    @Transient
    private Store store;

    @Transient
    private List<OrderItem> orderItems;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(int totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public int getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(int totalFail) {
        this.totalFail = totalFail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                ", address='" + address + '\'' +
                ", post='" + post + '\'' +
                ", receiver='" + receiver + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", createDate=" + createDate +
                ", payDate=" + payDate +
                ", deliveryDate=" + deliveryDate +
                ", confirmDate=" + confirmDate +
                ", uid=" + uid +
                ", express='" + express + '\'' +
                ", status='" + status + '\'' +
                ", sum=" + sum +
                ", quantity=" + quantity +
                ", payment='" + payment + '\'' +
                ", type=" + type +
                ", sid=" + sid +
                ", totalSum=" + totalSum +
                ", totalQuantity=" + totalQuantity +
                ", totalSuccess=" + totalSuccess +
                ", totalFail=" + totalFail +
                ", user=" + user +
                ", exchange=" + exchange +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                uid == order.uid &&
                sum == order.sum &&
                quantity == order.quantity &&
                type == order.type &&
                sid == order.sid &&
                totalSum == order.totalSum &&
                totalQuantity == order.totalQuantity &&
                totalSuccess == order.totalSuccess &&
                totalFail == order.totalFail &&
                Objects.equals(orderCode, order.orderCode) &&
                Objects.equals(address, order.address) &&
                Objects.equals(post, order.post) &&
                Objects.equals(receiver, order.receiver) &&
                Objects.equals(mobile, order.mobile) &&
                Objects.equals(userMessage, order.userMessage) &&
                Objects.equals(createDate, order.createDate) &&
                Objects.equals(payDate, order.payDate) &&
                Objects.equals(deliveryDate, order.deliveryDate) &&
                Objects.equals(confirmDate, order.confirmDate) &&
                Objects.equals(express, order.express) &&
                Objects.equals(status, order.status) &&
                Objects.equals(payment, order.payment) &&
                Objects.equals(user, order.user) &&
                Objects.equals(exchange, order.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderCode, address, post, receiver, mobile, userMessage, createDate, payDate, deliveryDate, confirmDate, uid, express, status, sum, quantity, payment, type, sid, totalSum, totalQuantity, totalSuccess, totalFail, user, exchange);
    }
}
