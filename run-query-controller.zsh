#! /bin/zsh

# Run the GraphQL Example Query from 'orders-graphql.json'

http POST localhost:9000/graphql-example < orders-graphql.json
