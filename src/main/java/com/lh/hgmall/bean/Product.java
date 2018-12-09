package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String name;

    private String subTitle;

    private Float originalPrice;

    private Float promotePrice;

    private int stock;

    private int cid;

    private Date createDate;

    private int status;

    private int sales;

    private int reviews;

    @Transient
    List<ProductImage> productImages;

    @Transient
    List<ProductImage> productSingleImages;

    @Transient
    List<ProductImage> productDetailImages;

    @Transient
    ProductImage firstProductImage;

    @Transient
    @JsonIgnoreProperties("products")
    Category category;

    @Transient
    @JsonIgnoreProperties("categories")
    Store store;

    @Transient
    @JsonIgnoreProperties("product")
    List<Review> reviewList;

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public List<ProductImage> getProductSingleImages() {
        return productSingleImages;
    }

    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public List<ProductImage> getProductDetailImages() {
        return productDetailImages;
    }

    public void setProductDetailImages(List<ProductImage> productDetailImages) {
        this.productDetailImages = productDetailImages;
    }

    public ProductImage getFirstProductImage() {
        return firstProductImage;
    }

    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Float getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(Float promotePrice) {
        this.promotePrice = promotePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", originalPrice=" + originalPrice +
                ", promotePrice=" + promotePrice +
                ", stock=" + stock +
                ", cid=" + cid +
                ", createDate=" + createDate +
                ", status=" + status +
                ", sales=" + sales +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id &&
                stock == product.stock &&
                cid == product.cid &&
                status == product.status &&
                sales == product.sales &&
                reviews == product.reviews &&
                Objects.equals(name, product.name) &&
                Objects.equals(subTitle, product.subTitle) &&
                Objects.equals(originalPrice, product.originalPrice) &&
                Objects.equals(promotePrice, product.promotePrice) &&
                Objects.equals(createDate, product.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subTitle, originalPrice, promotePrice, stock, cid, createDate, status, sales, reviews);
    }
}
