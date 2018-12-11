package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "store")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Store implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;

    private int cid;

    private String type;

    private Date applyDate;

    private Date addDate;

    private String applyName;

    private String mobile;

    private String telephone;

    private String identification;

    private String email;

    private int authentication_credit;

    private int authentication_id;

    private int authentication_phone;

    private String summary_html;

    private int status;

    @Transient
    @JsonIgnoreProperties({"stores","products","productRows","productShow"})
    private Category category;

    @Transient
    @JsonIgnoreProperties("store")
    private List<Category> categories;

    @Transient
    @JsonIgnoreProperties("store")
    private List<OrderItem> orderItems;


    @Transient
    @JsonIgnoreProperties("store")
    private List<Order> orders;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAuthentication_credit() {
        return authentication_credit;
    }

    public void setAuthentication_credit(int authentication_credit) {
        this.authentication_credit = authentication_credit;
    }

    public int getAuthentication_id() {
        return authentication_id;
    }

    public void setAuthentication_id(int authentication_id) {
        this.authentication_id = authentication_id;
    }

    public int getAuthentication_phone() {
        return authentication_phone;
    }

    public void setAuthentication_phone(int authentication_phone) {
        this.authentication_phone = authentication_phone;
    }

    public String getSummary_html() {
        return summary_html;
    }

    public void setSummary_html(String summary_html) {
        this.summary_html = summary_html;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cid=" + cid +
                ", type='" + type + '\'' +
                ", applyDate=" + applyDate +
                ", addDate=" + addDate +
                ", applyName='" + applyName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", telephone='" + telephone + '\'' +
                ", identification='" + identification + '\'' +
                ", email='" + email + '\'' +
                ", authentication_credit=" + authentication_credit +
                ", authentication_id=" + authentication_id +
                ", authentication_phone=" + authentication_phone +
                ", summary_html='" + summary_html + '\'' +
                ", status=" + status +
                ", category=" + category +
                ", categories=" + categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id == store.id &&
                cid == store.cid &&
                authentication_credit == store.authentication_credit &&
                authentication_id == store.authentication_id &&
                authentication_phone == store.authentication_phone &&
                status == store.status &&
                Objects.equals(name, store.name) &&
                Objects.equals(type, store.type) &&
                Objects.equals(applyDate, store.applyDate) &&
                Objects.equals(addDate, store.addDate) &&
                Objects.equals(applyName, store.applyName) &&
                Objects.equals(mobile, store.mobile) &&
                Objects.equals(telephone, store.telephone) &&
                Objects.equals(identification, store.identification) &&
                Objects.equals(email, store.email) &&
                Objects.equals(summary_html, store.summary_html) &&
                Objects.equals(category, store.category) &&
                Objects.equals(categories, store.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cid, type, applyDate, addDate, applyName, mobile, telephone, identification, email, authentication_credit, authentication_id, authentication_phone, summary_html, status, category, categories);
    }
}
