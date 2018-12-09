package com.lh.hgmall.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "power")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private int mid;

    private String title;

    private String text;

    private int score;

    private int exchange;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "Power{" +
                "id=" + id +
                ", mid=" + mid +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", exchange=" + exchange +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Power power = (Power) o;
        return id == power.id &&
                mid == power.mid &&
                score == power.score &&
                exchange == power.exchange &&
                Objects.equals(title, power.title) &&
                Objects.equals(text, power.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mid, title, text, score, exchange);
    }
}
