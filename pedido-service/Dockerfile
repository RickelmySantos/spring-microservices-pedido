FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

COPY target/pedido-service-0.0.1-SNAPSHOT.jar app.jar  

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]