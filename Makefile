# Include the .env file if it exists.
include .env

# Export the variables loaded from the .env file so that they are available as
# environment variables for any shell commands executed by this Makefile.
export

test:
	echo $$SERVICE_NAME

# Starts all containers defined in the Docker Compose file in detached mode.
# The detached mode (-d) runs the containers in the background.
start:
	docker compose up -d

# Stops and removes the containers defined in the Docker Compose file.
stop:
	docker compose down

# Restarts the containers
restart: stop start

# 1. Stops the containers and removes all associated volumes.
# 2. Removes all unused Docker volumes to free up disk space.
#    The -f flag forces the removal without prompting for confirmation.
# 3. Recreates and rebuilds the containers defined in the Docker Compose file.
#    It forces a recreation of containers and rebuilds their images.
rebuild:
	docker-compose down -v
	docker volume prune -f
	docker-compose up -d --force-recreate --build

dynamodb-up:
	aws dynamodb list-tables --region $(AWS_DYNAMODB_REGION) --endpoint-url $(AWS_DYNAMODB_ENDPOINT) | grep -w '"Comment"' > /dev/null \
	&& echo "Table 'Comment' already exists. Skipping creation." \
	|| ( \
		echo "Creating table 'Comment'..."; \
		aws dynamodb create-table \
			--table-name Comment \
			--attribute-definitions \
				AttributeName=postId,AttributeType=S \
				AttributeName=commentId,AttributeType=S \
			--key-schema \
				AttributeName=postId,KeyType=HASH \
				AttributeName=commentId,KeyType=RANGE \
			--global-secondary-indexes "$$(echo '[ \
				{ \
					"IndexName": "CommentIdGSI", \
					"KeySchema": [ { "AttributeName": "commentId", "KeyType": "HASH" } ], \
					"Projection": { "ProjectionType": "ALL" }, \
					"ProvisionedThroughput": { "ReadCapacityUnits": 5, "WriteCapacityUnits": 5 } \
				}, \
				{ \
					"IndexName": "PostIdGSI", \
					"KeySchema": [ { "AttributeName": "postId", "KeyType": "HASH" } ], \
					"Projection": { "ProjectionType": "ALL" }, \
					"ProvisionedThroughput": { "ReadCapacityUnits": 5, "WriteCapacityUnits": 5 } \
                } \
			]' | jq -c .)" \
			--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
			--region $(AWS_DYNAMODB_REGION) \
			--endpoint-url $(AWS_DYNAMODB_ENDPOINT); \
		echo "Table 'Comment' created successfully."; \
	)
