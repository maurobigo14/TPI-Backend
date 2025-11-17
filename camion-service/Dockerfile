## Etapa de build: compila el proyecto y genera el JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:resolve dependency:resolve-plugins
COPY src ./src
RUN mvn -q -DskipTests package

## Etapa final: imagen ligera para ejecutar el JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]