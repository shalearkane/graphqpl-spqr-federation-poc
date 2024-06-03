package com.chegg.federation.monolith;

import com.chegg.federation.monolith.model.Product;
import com.chegg.federation.monolith.repository.ProductRepository;
import graphql.kickstart.execution.context.DefaultGraphQLContext;
import graphql.kickstart.execution.context.GraphQLContext;
import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
import graphql.kickstart.servlet.context.DefaultGraphQLWebSocketContext;
import graphql.kickstart.servlet.context.GraphQLServletContextBuilder;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomGraphQLContextBuilder implements GraphQLServletContextBuilder {

    private final ProductRepository productRepository;

    public CustomGraphQLContextBuilder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public GraphQLContext build(HttpServletRequest req, HttpServletResponse response) {
        return DefaultGraphQLServletContext.createServletContext(buildDataLoaderRegistry(), null)
                .with(req).with(response)
                .build();
    }

    @Override
    public GraphQLContext build() {
        return new DefaultGraphQLContext(buildDataLoaderRegistry(), null);
    }

    @Override
    public GraphQLContext build(Session session, HandshakeRequest request) {
        return DefaultGraphQLWebSocketContext.createWebSocketContext(buildDataLoaderRegistry(), null)
                .with(session)
                .with(request).build();
    }

    private DataLoaderRegistry buildDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("productDataLoader",
                new DataLoader<String, Product>(productIds ->
                        CompletableFuture.supplyAsync(() ->
                                productRepository.getProductForIds(productIds))));
        return dataLoaderRegistry;
    }
}