FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

COPY target/pagamento-service-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]