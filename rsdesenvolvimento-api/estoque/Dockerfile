
FROM maven:3.9.6-eclipse-temurin-21 AS dependencies
WORKDIR /build

COPY pom.xml ./pom.xml
COPY estoque/pom.xml ./estoque/pom.xml

WORKDIR /build/estoque

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C -Pdeploy -DskipTests dependency:go-offline verify --fail-never


FROM dependencies AS build

COPY estoque/src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -T 1C -Pdeploy -DskipTests clean package && \
    java -Djarmode=tools -jar ./target/estoque-0.0.1-SNAPSHOT.jar \
    extract --layers --launcher --destination ./target/layers && \
    find target -mindepth 1 ! -path 'target/layers*' ! -name '*.jar' -exec rm -rf {} +


FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /build/estoque/target/layers/dependencies/ ./
COPY --from=build /build/estoque/target/layers/spring-boot-loader/ ./
COPY --from=build /build/estoque/target/layers/snapshot-dependencies/ ./
COPY --from=build /build/estoque/target/layers/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
