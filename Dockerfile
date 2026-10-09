FROM maven:3.9.16-amazoncorretto-25-alpine as build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean compile package -DskipTests

FROM amazoncorretto:25-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090

ENTRYPOINT [ "sh", "-c", "java -jar app.jar" ]