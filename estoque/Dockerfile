# Etapa de dependências
FROM maven:3.9.6-eclipse-temurin-21 as dependencies
WORKDIR /root

COPY parent-pom.xml pom.xml
WORKDIR /root/app
COPY pom.xml .
RUN --mount=type=cache,target=~/.m2 mvn dependency:go-offline -B -DskipTests && rm -rf ./target

# Etapa de build
FROM dependencies as build
COPY src src
RUN mvn clean package -DskipTests

# Imagem final
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /root/app/target/estoque-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
