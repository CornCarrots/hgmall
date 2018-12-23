package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "carousel")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Carousel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int sid;

    private int role;

    private String type;

    @Column(name = "status_")
    private String status;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Carousel{" +
                "id=" + id +
                ", sid=" + sid +
                ", role=" + role +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carousel carousel = (Carousel) o;
        return id == carousel.id &&
                sid == carousel.sid &&
                role == carousel.role &&
                Objects.equals(type, carousel.type) &&
                Objects.equals(status, carousel.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sid, role, type, status);
    }
}
