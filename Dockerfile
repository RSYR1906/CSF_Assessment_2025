# Stage 1 - Build Angular app
FROM node:20-alpine AS ngbuild

WORKDIR /client

# Install Angular CLI
RUN npm i -g @angular/cli@19.1.8

# Copy Angular configuration files
COPY client/angular.json .
COPY client/package.json .
COPY client/tsconfig.json .
COPY client/tsconfig.app.json .

# Copy source code
COPY client/src src

# Install dependencies and build
RUN npm i
RUN npm ci
RUN ng build

# Stage 2 - Build Spring Boot app
FROM eclipse-temurin:23-jdk-alpine AS javabuild

WORKDIR /server

# Copy Maven files
COPY server/pom.xml .
COPY server/.mvn .mvn
COPY server/mvnw .
COPY server/mvnw.cmd .

# Copy source code
COPY server/src src

# Create static directory if it doesn't exist
RUN mkdir -p src/main/resources/static

# Copy Angular build output to Spring Boot static resources directory
COPY --from=ngbuild /client/dist/client/ src/main/resources/static/

# Build Spring Boot app
RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

# Stage 3 - Final runtime image
FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

# Copy the built JAR file
COPY --from=javabuild /server/target/*.jar app.jar

# Environment variables for Railway
ENV PORT=8080

# Expose the port (but Railway will map this to its own port)
EXPOSE ${PORT}

ENTRYPOINT [ "java", "-jar", "app.jar"]