# Use Maven base image to compile the application
FROM maven:3.9.9-eclipse-temurin-23-alpine AS builder

# Set the working directory
WORKDIR /usr/src/app

# Define an argument to specify the Maven profile (By default dev profile)
# Avaliables:  
#   - dev: developement profile.
#   - devdocker: same as dev but using postgresql db instead h2.
#   - prod: production profile.
#
ARG BUILD_PROFILE=dev
ENV BUILD_PROFILE=${BUILD_PROFILE}

# Copy only the necessary files to download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -P${BUILD_PROFILE} --batch-mode

# Copy the rest of the application source code
COPY . .



# Build the application using the specified profile
RUN mvn clean install -P${BUILD_PROFILE}

# Use a lightweight Java runtime image for running the application
FROM eclipse-temurin:23-jre-alpine

ENV BUILD_PROFILE=dev

# Copy the generated JAR file from the builder stage

COPY --from=builder /usr/src/app/target/back-app-9.0.0-SNAPSHOT.jar /app/app.jar

# Expose ports
EXPOSE 8080
EXPOSE 443

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set user for run app.
USER spring 
# Command to run the Spring Boot application with the specified profile
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${BUILD_PROFILE} -jar /app/app.jar"]

