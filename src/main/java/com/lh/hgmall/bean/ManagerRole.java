package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "manager_role")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class ManagerRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int mid;

    private int rid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return "ManagerRole{" +
                "id=" + id +
                ", mid=" + mid +
                ", rid=" + rid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerRole that = (ManagerRole) o;
        return id == that.id &&
                mid == that.mid &&
                rid == that.rid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mid, rid);
    }
}
