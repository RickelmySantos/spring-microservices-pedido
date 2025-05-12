
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN mvn dependency:go-offline -B

COPY src ./src

# Compila o projeto
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

ENV TZ=UTC \
    LANG=en_US.UTF-8 \
    LANGUAGE=en_US:en \
    LC_ALL=en_US.UTF-8

RUN apt-get update && apt-get install -y curl

COPY --from=builder /app/target/eurekaserver-0.0.1-SNAPSHOT.jar /eurekaserver.jar

EXPOSE 8761

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/eurekaserver.jar"]
