# Use Maven base image to compile the application
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Set the working directory
WORKDIR /usr/src/app

# Define an argument to specify the Maven profile
ARG BUILD_PROFILE=dev

# Copy only the necessary files to download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -P${BUILD_PROFILE}

# Copy the rest of the application source code
COPY . .

# Build the application using the specified profile
RUN mvn clean install -P${BUILD_PROFILE}

# Use a lightweight Java runtime image for running the application
FROM eclipse-temurin:21-jre-alpine

# Copy the generated JAR file from the builder stage

COPY --from=builder /usr/src/app/target/back-app-7.1.0-RELEASE.jar /app/app.jar

# Expose ports
EXPOSE 8080
EXPOSE 443

# Optional: Copy certificates if needed
ARG COPY_CERTIFICATES=false
RUN if [ "$COPY_CERTIFICATES" = "true" ]; then \
      mkdir -p /etc/ssl/certs/xadreznaron.es /etc/ssl/certs/www.xadreznaron.es && \
      cp /certificates/xadreznaron.es/fullchain.pem /etc/ssl/certs/xadreznaron.es/ && \
      cp /certificates/xadreznaron.es/privkey.pem /etc/ssl/certs/xadreznaron.es/ && \
      cp /certificates/xadreznaron.es/keystore.p12 /etc/ssl/certs/xadreznaron.es/ && \
      cp /certificates/xadreznaron.es/newkey.jks /etc/ssl/certs/xadreznaron.es/ && \
      cp /certificates/www.xadreznaron.es/fullchain.pem /etc/ssl/certs/www.xadreznaron.es/ && \
      cp /certificates/www.xadreznaron.es/privkey.pem /etc/ssl/certs/www.xadreznaron.es/; \
    fi

# Set an environment variable to choose the Spring profile at runtime
ENV PROFILE=dev

# Command to run the Spring Boot application with the specified profile
CMD ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "/app/app.jar"]
