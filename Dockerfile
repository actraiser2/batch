FROM maven:3.9-eclipse-temurin AS build
COPY . /src/
WORKDIR /src

RUN mvn --batch-mode clean package

FROM eclipse-temurin:17
COPY --from=build /src/target/*.jar /app/batch.jar

EXPOSE 8080
WORKDIR /app
ENTRYPOINT ["java", "-jar", "batch.jar"]
