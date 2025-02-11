# aws-dynamodb-spring-module

This module provides a local setup for DynamoDB using LocalStack and a Spring Boot configuration for seamless integration. It ensures data persistence and includes a DynamoDB Web Admin UI for easy database management.

### Running DynamoDB Locally

#### Start DynamoDB and Admin UI
```sh
make start
```

#### Access the DynamoDB Web Admin UI at: http://localhost:8001

All data is persisted in the volume directory.

#### Stop the Services
```sh
make stop
```
