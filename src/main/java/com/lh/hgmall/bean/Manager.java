package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "manager")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int sid;

    private String name;

    private String password;

    private String salt;

    private String nickName;

    private int sex;

    private String mobile;

    private String email;

    private Date createDate;

    private int status;

    @Transient
    private List<Role> roles;

    @Transient
    private Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", sid=" + sid +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return id == manager.id &&
                sid == manager.sid &&
                sex == manager.sex &&
                status == manager.status &&
                Objects.equals(name, manager.name) &&
                Objects.equals(password, manager.password) &&
                Objects.equals(salt, manager.salt) &&
                Objects.equals(nickName, manager.nickName) &&
                Objects.equals(mobile, manager.mobile) &&
                Objects.equals(email, manager.email) &&
                Objects.equals(createDate, manager.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sid, name, password, salt, nickName, sex, mobile, email, createDate, status);
    }
}
