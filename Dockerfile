# Build the application and run tests
FROM maven:3.9.5-openjdk-17 AS build
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .
COPY src ./src

# Run tests during the build
RUN mvn clean test

# Package the application
RUN mvn clean package -DskipTests

# Create image
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the JAR file
COPY --from=build /app/target/vehicles-service.jar app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
