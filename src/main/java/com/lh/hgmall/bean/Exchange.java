package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exchange")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int oid;

    @Column(name = "desc_")
    private String describe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", oid=" + oid +
                ", describe='" + describe + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exchange exchange = (Exchange) o;
        return id == exchange.id &&
                oid == exchange.oid &&
                Objects.equals(describe, exchange.describe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oid, describe);
    }
}
