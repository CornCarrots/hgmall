package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "logistics")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Logistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private Date date;

    private String location;

    private int oid;

    @Transient
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return "Logistics{" +
                "id=" + id +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", oid=" + oid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Logistics logistics = (Logistics) o;
        return id == logistics.id &&
                oid == logistics.oid &&
                Objects.equals(date, logistics.date) &&
                Objects.equals(location, logistics.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, location, oid);
    }
}
