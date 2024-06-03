package com.chegg.federation.monolith.query;

import com.chegg.federation.monolith.model.Product;
import com.chegg.federation.monolith.model.Review;
import com.chegg.federation.monolith.model.User;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    public List<Product> products = new ArrayList<>();
    public List<Review> reviews = new ArrayList<>();
    public List<User> endorsedBy = new ArrayList<>();


    public ProductService() {
        products.add(new Product("1"));
        products.add(new Product("2"));
        products.add(new Product("3"));

        reviews.add(new Review("1","Love it!", new User("1", "Sam"), new Product("1")));
        reviews.add(new Review("2","Too expensive.", new User("1", "Sam"), new Product("2")));
        reviews.add(new Review("3","Could be better.", new User("2", "Ram"), new Product("3")));
        reviews.add(new Review("4","Prefer something else.", new User("2", "Ram"), new Product("1")));

        User user1 = new User("1", "Ram");
        user1.setProduct(new Product("1"));
        endorsedBy.add(user1);

        User user2 = new User("2", "Shyam");
        user2.setProduct(new Product("1"));
        endorsedBy.add(user2);

        User user3 = new User("3", "Radha");
        user3.setProduct(new Product("1"));
        endorsedBy.add(user3);
    }

    @GraphQLQuery(name = "reviews")
    public List<Review> reviews(@GraphQLContext Product product) {
        return reviews;
    }
    public Product find(String upc){
        System.out.println("Getting hit by a raging federating server");
        Product product1 = new Product(upc, endorsedBy);
        List<User> setWithEndorsement = new LinkedList<>();
        for(User u : product1.getEndorsedBy()) {
            u.setProduct(product1);
            setWithEndorsement.add(u);
        }

        product1.setEndorsedBy(setWithEndorsement);
        System.out.println(product1.getEndorsedBy());
        return product1;
    }
}
