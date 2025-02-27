# aws-dynamodb-spring-module

This module provides a local setup for DynamoDB using LocalStack and a Spring Boot configuration for seamless integration. It ensures data persistence and includes a DynamoDB Web Admin UI for easy database management.

### Running DynamoDB Locally

#### Start DynamoDB and Admin UI
```sh
make start
make dynamodb-up
```

#### Access the DynamoDB Web Admin UI at: http://localhost:8001

All data is persisted in the volume directory.

#### Stop the Services
```sh
make stop
```

### App APIs

#### Create
```curl
curl --location 'http://localhost:8080/api/comment/create' \
--header 'Content-Type: application/json' \
--data '{
    "postId": "01",
    "text": "This is 02 my comment",
    "author": "tester"
}'
```

#### Get All Comments
```curl
curl --location 'http://localhost:8080/api/comment/getAllComments'
```

#### Get By CommentId
```curl
curl --location 'http://localhost:8080/api/comment/getByCommentId?commentId=?' \
--data ''
```

#### Get By PostId
```curl
curl --location 'http://localhost:8080/api/comment/getCommentsByPostId?postId=01' \
--data ''
```

#### Update
```curl
curl --location --request PUT 'http://localhost:8080/api/comment/update?commentId=?' \
--header 'Content-Type: application/json' \
--data '{
    "postId": "01",
    "text": "This is 03 my comment",
    "author": "tester"
}'
```

#### Delete
```curl
curl --location --request DELETE 'http://localhost:8080/api/comment/deleteByCommentId?commentId=?' \
--header 'Content-Type: application/json' \
--data ''
```
