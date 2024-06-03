package com.chegg.federation.monolith.query;

import com.chegg.federation.monolith.model.Product;
import com.chegg.federation.monolith.model.Review;
import com.chegg.federation.monolith.model.User;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.stereotype.Component;

@Component
public class ReviewQuery {

    @GraphQLQuery(name = "trivia")
    public Review trivia(@GraphQLArgument(name = "id") String id) {
        return new Review("1","Love it!", new User("1"), new Product("1"));
    }
}
