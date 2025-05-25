### DOWNLOAD DEPENDENCIES
FROM maven:3.9.6-eclipse-temurin-21 AS dependencies
WORKDIR /root
COPY parent-pom.xml pom.xml

WORKDIR /root/app
COPY pom.xml .
RUN --mount=type=cache,target=~/.m2/repository mvn -B -T 1C -Dmaven.artifact.threads=25 dependency:go-offline verify --fail-never -DskipTests && rm -rf ./target

### BUILD APP

FROM dependencies AS build 
COPY src src 
RUN --mount=type=cache,target=~/app/target \
    mvn -B -T 1C -Pdeploy -DskipTests clean package && \
    java -Djarmode=tools -jar ./target/estoque-0.0.1-SNAPSHOT.jar \
    extract --layers --launcher --destination ./target/layers && \
    rm -rf ~/.m2/repository && \
    find target -mindepth 1 ! -path 'target/layers*' ! -name '*.jar' -exec rm -rf {} +


### BUILD DOCKER IMAGE

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

### COPY LAYERS
COPY --from=build /root/app/target/layers/dependencies/ ./
COPY --from=build /root/app/target/layers/spring-boot-loader/ ./
COPY --from=build /root/app/target/layers/snapshot-dependencies/ ./
COPY --from=build /root/app/target/layers/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]