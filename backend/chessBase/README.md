# CHESSBASE

## Before start

### BUILD 

build image with command:
```shell
docker build -t chessbase .
```

### RUN

run postgreSQL on your localhost and set password 
```shell
docker run -e POSTGRES_PASSWORD=postgrespw -e PORT=5432 -e POSTGRES_DB=chessbase --name postgres -p 5432:5432 -d postgres
```

and then run an application with command:
```shell
docker run -e PORT=9000 -e POSTGRES_URL=host.docker.internal -e DB_NAME=chessbase -p 9000:9000 chessbase
```

## DOCKER COMPOSE

run this project in docker compose

```shell
docker compose down -v && docker compose up --force-recreate -d
```

# test
