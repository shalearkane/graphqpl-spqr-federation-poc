package com.chegg.federation.user.model;

import directives.FederationExtendsDirective;
import directives.FederationExternalDirective;
import directives.FederationKeyDirective;

import java.util.List;

@FederationKeyDirective(fields = "upc")
@FederationExtendsDirective
public class Product {

    @FederationExternalDirective
    private String upc;
    private List<User> endorsedBy;

    public Product(final String upc, final List<User> users) {
        this.upc = upc;
        this.endorsedBy = users;
    }

    public Product(final String upc) {
        this.upc = upc;
    }

    public Product() {
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(final String upc) {
        this.upc = upc;
    }

    public List<User> getEndorsedBy() {
        return endorsedBy;
    }

    public void setEndorsedBy(final List<User> endorsedBy) {
        this.endorsedBy = endorsedBy;
    }
}