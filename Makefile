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
