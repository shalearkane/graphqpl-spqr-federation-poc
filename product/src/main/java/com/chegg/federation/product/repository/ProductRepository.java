package com.chegg.federation.product.repository;

import com.chegg.federation.product.model.Product;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

@Repository
public class ProductRepository {
    private static final String consonants = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final String vowels = "AEIOU";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomWord(int length) {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < length; i++) {
            String characters = (i % 2 == 0) ? consonants : vowels;
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }


    public static List<Product> getProductForIds(List<String> productIds) {
        List<Product> ls = new LinkedList<>();
        for(String p : productIds) {
            ls.add(new Product(p, generateRandomWord(4), 1));
        }

        return ls;
    }
}
