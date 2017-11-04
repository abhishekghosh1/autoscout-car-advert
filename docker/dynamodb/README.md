DynamoDB Local image
=====================

> Runs a local copy of dynamoDB, automatically provisioned with a table upon startup

## Usage:

```sh
$ docker run -d --rm -p 8000:8000 --name dynamodb cnadiminti/dynamodb-local:latest
```
## Shell access

The dynamoDB exposes a shell on http://localhost:8000/shell/, where port is where the dynamoDB container is listening

To create an table use
```
var params = {
    TableName: 'carAdvert',
    KeySchema: [
        {   AttributeName: 'id',
            KeyType: 'HASH',
        }
    ],
    AttributeDefinitions: [
        {
            AttributeName: 'id',
            AttributeType: 'S',
        }
    ],
    ProvisionedThroughput: { // required provisioned throughput for the table
        ReadCapacityUnits: 1,
        WriteCapacityUnits: 1,
    }
};
dynamodb.createTable(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response

});
```

For example, to view all items in a table, use

```js
var params = {
    TableName: "carAdvert"
};

docClient.scan(params, function(err, data) {
    if (err)
        console.log(JSON.stringify(err, null, 2));
    else
        console.log(JSON.stringify(data, null, 2));
});
```