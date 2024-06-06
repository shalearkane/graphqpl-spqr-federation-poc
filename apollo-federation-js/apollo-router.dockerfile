FROM ghcr.io/apollographql/router:v1.48.0

COPY ./supergraph.graphql schema.graphql

ENTRYPOINT ["./router","--supergraph", "schema.graphql" ]