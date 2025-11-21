FROM chainguard/gradle:latest AS build
WORKDIR /app
COPY gradlew /.
COPY src /src
COPY gradle /gradle
COPY build.gradle.kts /.
COPY settings.gradle.kts /.
RUN ./gradlew jar

FROM eclipse-temurin:24-jre-alpine AS run
COPY --from=build /app/build/libs/Hyperstream-Leonardo.jar .
ENV ENVIRONMENT="prod"
ENTRYPOINT java -jar Hyperstream-Leonardo.jar
