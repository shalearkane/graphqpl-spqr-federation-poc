package com.chegg.federation.monolith.query;

import com.chegg.federation.monolith.model.Review;
import com.chegg.federation.monolith.model.User;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery {

    @Autowired
    private UserService userService;

    @GraphQLQuery(name = "getUserById")
    public User getUser(@GraphQLArgument(name = "id") String id){
        return userService.lookupUser(id);
    }

    @GraphQLQuery(name = "some")
    public User getSomeUser(@GraphQLContext Review review) {
        return new User("1", "something");
    }
}
