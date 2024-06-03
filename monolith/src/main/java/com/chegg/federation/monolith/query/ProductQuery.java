package com.chegg.federation.monolith.query;

import com.chegg.federation.monolith.model.Product;
import com.chegg.federation.monolith.repository.ProductRepository;
import graphql.kickstart.execution.context.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class ProductQuery {


    @GraphQLQuery(name = "getProductsInStock")
    public Set<Product> getProductsInStock(){
        Set<Product> mockResult = new HashSet<>();
        Product product1 = new Product("1","Table", 899);
        mockResult.add(product1);
        Product product2 = new Product("2","Couch", 500);
        mockResult.add(product2);
        return mockResult;
    }

    @GraphQLQuery(name = "getProductByID")
    public CompletableFuture<Product> getProduct(@GraphQLArgument(name="id") String upc, @GraphQLEnvironment ResolutionEnvironment env) {
        DataLoaderRegistry registry = ((GraphQLContext) env.dataFetchingEnvironment.getContext()).getDataLoaderRegistry();
        DataLoader<String, Product> productLoader = registry.getDataLoader("productDataLoader");
        if (productLoader != null) {
            return productLoader.load(upc);
        }
        throw new IllegalStateException("No product data loader found");
    }

    @GraphQLQuery(name = "getProducts")
    public List<Product> getProducts(@GraphQLEnvironment ResolutionEnvironment env) {
        List<Product> lp = new ArrayList<>();

        for(Integer i = 0; i<10; i++) {
            ProductService p = new ProductService();
            lp.add(p.find("1"));
        }

        return lp;
    }


}
