FROM maven:3.9-eclipse-temurin-25-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY src/ ./src

RUN mvn clean -DskipTests package

FROM amazoncorretto:25-alpine3.20

WORKDIR /guilda

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
