# build angular app
FROM node:22 AS ngbuild

WORKDIR /client

# install angular cli
RUN npm i -g @angular/cli@19.2.1

COPY ecommerce/client/angular.json .
COPY ecommerce/client/package.json .
COPY ecommerce/client/tsconfig.json .
COPY ecommerce/client/tsconfig.app.json .
#COPY client/server.ts .
#COPY client/ngsw-config.json .
COPY ecommerce/client/src src

RUN npm i
RUN npm ci
RUN ng build

# Stage 2 - build spring boot
FROM openjdk:23 AS javabuild

WORKDIR /server

COPY ecommerce/pom.xml .
COPY ecommerce/.mvn .mvn
COPY ecommerce/mvnw .
COPY ecommerce/src src

COPY --from=ngbuild /client/dist/client-side/browser src/main/resources/static

RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

## RUN container
FROM openjdk:23

WORKDIR /app

COPY --from=javabuild /server/target/*.jar app.jar

ENV PORT=8080

EXPOSE ${PORT}

# start container
ENTRYPOINT [ "java", "-jar", "app.jar"]