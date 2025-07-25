FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src/main ./src/main
RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.2-alpine3.19
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]