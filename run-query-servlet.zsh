#! /bin/zsh


FILE=orders-graphql.json

if [ ! -z "$1" ]; then
  FILE=$1
fi;

http POST localhost:9000/graphql < orders-graphql.json
