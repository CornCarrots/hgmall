package com.lh.hgmall.util;

import com.lh.hgmall.bean.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtil {
    public static List<Product> sort(List<Product> products, String order) {
        Comparator<Product> all = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Comparator<Product> review = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getReviews() - o2.getReviews();
            }
        };
        Comparator<Product> date = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        };
        Comparator<Product> sale = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getSales() - o2.getSales();
            }
        };
        Comparator<Product> price = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if (o1.getPromotePrice() - o2.getPromotePrice() > 0)
                    return 1;
                else if (o1.getPromotePrice() - o2.getPromotePrice() < 0)
                    return -1;
                return 0;
            }
        };
        Comparator<Product> comparator = null;
        switch (order) {
            case "all":
                comparator = all;
                break;
            case "review":
                comparator = review;
                break;
            case "date":
                comparator = date;
                break;
            case "sale":
                comparator = sale;
                break;
            case "price":
                comparator = price;
                break;
        }
        Collections.sort(products, comparator);
        return products;
    }

}
