package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "option_")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Option implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;

    private String icon;

    @Column(name = "key_")
    private String key;

    @Column(name = "desc_")
    private String desc;

    @Column(name = "right_")
    private String right;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", key='" + key + '\'' +
                ", desc='" + desc + '\'' +
                ", right='" + right + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Option option = (Option) o;
        return id == option.id &&
                Objects.equals(name, option.name) &&
                Objects.equals(icon, option.icon) &&
                Objects.equals(key, option.key) &&
                Objects.equals(desc, option.desc) &&
                Objects.equals(right, option.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, icon, key, desc, right);
    }
}
