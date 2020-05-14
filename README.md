# my-sensitive-webapp
For demo purpose, this is an app that is sensitive to RCE.
Note: 10.128.76.174 is randomized: replace by any relevant IP
Exploits are:
* RCE using cookie content (jackson lib)
* RCE using JWT forged body (jjwt lib)

## Run
```
// locally
docker run -t -p 8080:8080 adioss/sensitive-webapp:latest
docker run -t -p 12345:12345 adioss/sensitive-microservice:latest
// with some params (replace 10.128.76.174 by real ip)
docker run -d --rm -p 8080:8080 -p 8090-8100:8090-8100 -e DEFAULT_MICRO_SERVICE_URL=http://10.128.76.174:12345 adioss/sensitive-webapp:latest
docker run -d --rm -p 12345:12345 -p 8190-8200:8190-8200 -e DEFAULT_WEBAPP_URL=http://10.128.76.174:8080 adioss/sensitive-microservice:latest
```

## Build
```
mvn install dockerfile:build
// not working mvn install dockerfile:build dockerfile:push
docker push adioss/sensitive-webapp:latest
docker push adioss/sensitive-microservice:latest
```