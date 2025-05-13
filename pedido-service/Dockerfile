FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app

COPY target/pedido-service-0.0.1-SNAPSHOT.jar app.jar  

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]


# # Etapa 1: Build da aplicação
# FROM maven:3.9.4-eclipse-temurin-21 AS builder

# WORKDIR /app

# # Copia o parent POM da raiz do projeto (você precisa injetar via docker-compose ou copiar antes)
# COPY ../pom.xml ./parent-pom.xml

# # Copia o pom do módulo
# COPY pom.xml .

# # Renomeia temporariamente o parent POM para pom.xml para resolver dependências comuns
# RUN cp parent-pom.xml parent-temp.xml && cp pom.xml pom-temp.xml && \
#     cp parent-pom.xml pom.xml && \
#     mvn -B -Dmaven.repo.local=/root/.m2 dependency:go-offline || true && \
#     mv pom-temp.xml pom.xml

# # Copia o código-fonte
# COPY src ./src

# # Build do jar
# RUN mvn -B clean package -DskipTests

# # Etapa 2: imagem final
# FROM eclipse-temurin:21-jre-alpine

# WORKDIR /app
# COPY --from=builder /app/target/*.jar app.jar

# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]
