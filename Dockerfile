# ---------- Build stage ----------
FROM gradle:8.14-jdk21 AS build
WORKDIR /app

# Copy Gradle files first for better caching
# Copy the Gradle wrapper and other necessary files
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY src /app/src

# Download dependencies (cached)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src src

# Build Spring Boot jar
RUN gradle bootJar --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar /app/institute-listing.jar

# Spring Boot default port
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:InitialRAMPercentage=50", "-XX:MaxRAMPercentage=75","-jar","institute-listing.jar"]
