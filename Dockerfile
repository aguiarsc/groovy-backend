FROM eclipse-temurin:21-jdk as build
WORKDIR /app

# Copy maven files first for better caching
COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./

# Make maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Create directories for persistent data
RUN mkdir -p /app/data /app/uploads

# Copy the uploads directory from your local machine to the container
# This will copy all your existing images into the container
COPY uploads/ /app/uploads/

# Set proper permissions for the uploads directory
RUN chmod -R 755 /app/uploads

# Expose port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
