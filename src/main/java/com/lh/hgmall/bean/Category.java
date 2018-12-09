package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;

    private int sid;

    private String type;

    private String icon;

    @Transient
    @JsonIgnoreProperties("categories")
    private Store store;

    @Transient
    private List<Property> properties;

    @Transient
    @JsonIgnoreProperties("category")
    private List<Product> products;

    @Transient
    @JsonIgnoreProperties("category")
    private List<Store> stores;

    @Transient
    private List<List<Product>> productRows;

    @Transient
    @JsonIgnoreProperties("category")
    private List<Product> productShow;

    @Transient
    private int imgID;

    @Transient
    @JsonIgnoreProperties("category")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public List<Product> getProductShow() {
        return productShow;
    }

    public void setProductShow(List<Product> productShow) {
        this.productShow = productShow;
    }

    public List<List<Product>> getProductRows() {
        return productRows;
    }

    public void setProductRows(List<List<Product>> productRows) {
        this.productRows = productRows;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
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


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sid=" + sid +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id &&
                sid == category.sid &&
                Objects.equals(name, category.name) &&
                Objects.equals(type, category.type) &&
                Objects.equals(icon, category.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sid, type, icon);
    }
}
