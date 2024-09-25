# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY mvnw .
COPY pom.xml .

# Copy the .mvn directory, required for the Maven wrapper
COPY .mvn .mvn

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port that Spring Boot will run on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/megastore-0.0.1-SNAPSHOT.jar"]
