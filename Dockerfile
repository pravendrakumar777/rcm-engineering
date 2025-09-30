# Use lightweight JDK image
FROM openjdk:17-jdk-alpine

# Working directory inside container
WORKDIR /app

# Copy built jar
COPY target/rcm-engineering-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8081

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]