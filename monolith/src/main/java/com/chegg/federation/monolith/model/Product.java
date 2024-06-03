package com.chegg.federation.monolith.model;

import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.List;
import java.util.Objects;

public class Product {
    private String upc;
    private String name;
    private Integer price;
    private List<Review> reviews;
    private List<User> endorsedBy;

    public Product() {
    }

    public Product(final String upc, final List<User> users) {
        this.upc = upc;
        this.endorsedBy = users;
    }

    public Product(String upc, String name, Integer price) {
        this.upc = upc;
        this.name = name;
        this.price = price;
    }

    public Product(String upc) {
        this.upc = upc;
        this.name = "random";
        this.price = 20;
    }

    public static Product lookup() {
        return new Product("vand", "aliz", 1);
    }

    @GraphQLQuery(name = "upc")
    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    @GraphQLQuery(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @GraphQLQuery(name = "price")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return Objects.equals(upc, product.upc);

    }

    @Override
    public int hashCode() {
        return upc != null ? upc.hashCode() : 0;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(final List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<User> getEndorsedBy() {
        return endorsedBy;
    }
    public void setEndorsedBy(final List<User> endorsedBy) {
        this.endorsedBy = endorsedBy;
    }
}
