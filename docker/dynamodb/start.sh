#!/bin/bash

set -o nounset
set -o errexit

set -m
/usr/bin/java -Djava.library.path=. -jar DynamoDBLocal.jar -dbPath /var/dynamodb_local
ls -lrt

sleep 10

# Provision de dynamoDB table
aws dynamodb create-table \
    --table-name $TABLE \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
	--key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --region=eu-west-1 \
    --endpoint=http://localhost:8000

# Bring dynamoDB process to the foreground
fg