# Use a lightweight JDK image for building
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mkdir -p src && echo "" > src/.keep

# Download dependencies (caches unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Use a lightweight JRE image for running
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (optional, default Spring Boot port)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar"]