# build angular app
FROM node:22 AS ngbuild

WORKDIR /client

# install angular cli
RUN npm i -g @angular/cli@19.2.1

COPY csf_assessment_template/client/angular.json .
COPY csf_assessment_template/client/package.json .
COPY csf_assessment_template/client/tsconfig.json .
COPY csf_assessment_template/client/tsconfig.app.json .
#COPY client/server.ts .
#COPY client/ngsw-config.json .
COPY client/src src

RUN npm i
RUN npm ci
RUN ng build

# Stage 2 - build spring boot
FROM openjdk:23 AS javabuild

WORKDIR /server

COPY csf_assessment_template/pom.xml .
COPY csf_assessment_template/.mvn .mvn
COPY csf_assessment_template/mvnw .
COPY csf_assessment_template/src src

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