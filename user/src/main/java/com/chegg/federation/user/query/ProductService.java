package com.chegg.federation.user.query;

import com.chegg.federation.user.model.Product;
import com.chegg.federation.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    public List<Product> products = new ArrayList<>();
    public List<User> endorsedBy = new ArrayList<>();

    public ProductService() {
        products.add(new Product("1"));
        products.add(new Product("2"));
        products.add(new Product("3"));

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

    public Product lookupProduct(String upc) {
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