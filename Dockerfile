FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle.properties .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
RUN ./gradlew --no-daemon dependencies || true
COPY src src
RUN ./gradlew --no-daemon jar

FROM eclipse-temurin:21-jre-alpine AS run
COPY --from=build /app/build/*.jar Hyperstream-Leonardo.jar
ARG ENVIRONMENT="prod"
ENV ENVIRONMENT=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "Hyperstream-Leonardo.jar"]
EXPOSE 8881
