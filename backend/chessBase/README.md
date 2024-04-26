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
docker run -e POSTGRES_PASSWORD=postgrespw -e PORT=5432 -p 5432:5432 postgres -d
```

and then run an application with command:
```shell
docker run -e PORT=9000 -p 9000:9000 --network host chessbase
```

## DOCKER COMPOSE

run this project in docker compose

```shell
docker compose down -v && docker compose up --force-recreate -d
```