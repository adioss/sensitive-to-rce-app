# sensitive-to-rce-app

## Run first challenge
```
// locally
docker run -t -p 8080:8080 -p 8090:8090 adioss/sensitive-webapp:latest
```

## Run second challenge
```
// locally
docker run -t -p 8080:8080 -e HACK_ACTIVATED=false adioss/sensitive-webapp:latest
docker run -t -p 12345:12345 -p 8090:8090  adioss/sensitive-microservice:latest
// with some params
docker run -d --rm -p 8080:8080 -e BYPASS_LOGIN="you" -e BYPASS_PASSWORD="rocks" -e HACK_ACTIVATED=false -e DEFAULT_MICRO_SERVICE_URL=http://xx.xx.xx.xx:12345 adioss/sensitive-webapp:latest
docker run -d --rm -p 12345:12345 -p 8190-8200:8190-8200 -e DEFAULT_WEBAPP_URL=http://xx.xx.xx.xx:8080 adioss/sensitive-microservice:latest
```

## Build
```
mvn install dockerfile:build
// not working mvn install dockerfile:build dockerfile:push
docker push adioss/sensitive-webapp:latest
docker push adioss/sensitive-microservice:latest
```
