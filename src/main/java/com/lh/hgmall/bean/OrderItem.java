package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "orderitem")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int pid;

    private int oid;

    private int uid;

    private int number;

    private int sid;

    private int review;


    @Transient
    @JsonIgnoreProperties({"category","store"})
    private Product product;

    @Transient
    private Order order;

    @Transient
    private User user;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }



    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", pid=" + pid +
                ", oid=" + oid +
                ", uid=" + uid +
                ", number=" + number +
                ", sid=" + sid +
                ", review=" + review +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem item = (OrderItem) o;
        return id == item.id &&
                pid == item.pid &&
                oid == item.oid &&
                uid == item.uid &&
                number == item.number &&
                sid == item.sid &&
                review == item.review;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pid, oid, uid, number, sid, review);
    }
}
