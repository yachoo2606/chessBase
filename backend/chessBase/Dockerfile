FROM maven:3.9.5-eclipse-temurin-17-alpine as builder
LABEL authors="Jan Biały - yachoo2606"


# Set the working directory in the container
WORKDIR /app
COPY src src
COPY ./mvnw mvnw
COPY ./mvnw.cmd mvnw.cmd
COPY ./pom.xml pom.xml


RUN mvn clean install -DskipTests

FROM openjdk:17-alpine3.14

COPY --from=builder /app/target/*.jar /app/executables/ChessBase.jar
WORKDIR /app/executables

CMD ["java","-jar","ChessBase.jar"]