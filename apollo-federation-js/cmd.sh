rover supergraph compose --output ./supergraph.graphql --config ./supergraph-config.yaml
./router --supergraph supergraph.graphql


docker build --tag router -f apollo-router.dockerfile . && docker run -p 4000:4000 router