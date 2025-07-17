### Etapa 1: RESOLVER DEPENDÊNCIAS
FROM maven:3.9.6-eclipse-temurin-21 AS dependencies
WORKDIR /build

# Copia o parent POM do projeto principal (assumindo contexto na raiz)
COPY pom.xml ./pom.xml
COPY usuario/pom.xml ./usuario/pom.xml

WORKDIR /build/usuario

# Baixar dependências offline (cache do .m2 ativado)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C -Pdeploy -DskipTests dependency:go-offline verify --fail-never


### Etapa 2: COMPILAR O PROJETO
FROM dependencies AS build

# Copia o código-fonte do serviço
COPY usuario/src ./src

# Compila o projeto
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C -Pdeploy -DskipTests clean package && \
    java -Djarmode=tools -jar ./target/usuario-0.0.1-SNAPSHOT.jar \
    extract --layers --launcher --destination ./target/layers && \
    find target -mindepth 1 ! -path 'target/layers*' ! -name '*.jar' -exec rm -rf {} +


### Etapa 3: MONTAR A IMAGEM FINAL
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copia camadas otimizadas do Spring Boot
COPY --from=build /build/usuario/target/layers/dependencies/ ./
COPY --from=build /build/usuario/target/layers/spring-boot-loader/ ./
COPY --from=build /build/usuario/target/layers/snapshot-dependencies/ ./
COPY --from=build /build/usuario/target/layers/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
