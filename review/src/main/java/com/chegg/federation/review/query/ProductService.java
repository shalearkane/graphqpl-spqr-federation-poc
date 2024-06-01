package com.chegg.federation.review.query;

import com.chegg.federation.review.model.Product;
import com.chegg.federation.review.model.Review;
import com.chegg.federation.review.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    public List<Product> products = new ArrayList<>();
    public List<Review> reviews = new ArrayList<>();

    public ProductService() {
        products.add(new Product("1"));
        products.add(new Product("2"));
        products.add(new Product("3"));

        reviews.add(new Review("1","Love it!", new User("1"), new Product("1")));
        reviews.add(new Review("2","Too expensive.", new User("1"), new Product("2")));
        reviews.add(new Review("3","Could be better.", new User("2"), new Product("3")));
        reviews.add(new Review("4","Prefer something else.", new User("2"), new Product("1")));
    }

    public Product lookupProduct(String upc) {
        System.out.println("Getting hit by a raging federating server");
        Product product1 = new Product(upc);
        product1.setReviews(reviews.stream()
                .collect(Collectors.toList()));
        return product1;
    }
}
