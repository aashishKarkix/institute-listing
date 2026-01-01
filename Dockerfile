# ---------- Build stage ----------
FROM gradle:8.14-jdk21-alpine AS build
#RUN apk add --no-cache bash
WORKDIR /app

COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
RUN gradle dependencies --no-daemon
COPY src src

RUN gradle dependencies

RUN gradle bootJar --no-daemon

RUN rm -rf ~/.gradle/caches

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/institute-listing.jar

EXPOSE 8080

# Run app
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:InitialRAMPercentage=50", "-XX:MaxRAMPercentage=75","-jar","institute-listing.jar"]
