# Use Maven base image to compile the application
FROM maven:3.9.9-eclipse-temurin-23 AS builder

# Set the working directory
WORKDIR /usr/src/app

# Define an argument to specify the Maven profile (By default dev profile)
# Avaliables:  
#   - dev: developement profile.
#   - devdocker: same as dev but using postgresql db instead h2.
#   - prod: production profile.
#
ENV BUILD_PROFILE=dev

# Copy only the necessary files to download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -P${BUILD_PROFILE} --batch-mode

# Copy the rest of the application source code
COPY . .



# Build the application using the specified profile
RUN mvn clean install -P${BUILD_PROFILE}

# Use a lightweight Java runtime image for running the application
FROM eclipse-temurin:23-jre

ENV SPRING_UID=1000
ENV SPRING_GID=1000
ENV STORAGE_LOCATION_PATH=/home/appStorage
ENV BUILD_PROFILE=dev


# Copy the generated JAR file from the builder stage

COPY --from=builder /usr/src/app/target/back-app-9.0.0-RELEASE.jar /app/app.jar

# Expose ports
EXPOSE 8080
EXPOSE 443

# Crear grupo y usuario con UID y GID específicos
RUN addgroup -S -g ${SPRING_GID} spring && \
    adduser -S -u ${SPRING_UID} -G spring spring

# Crear el directorio y dar permisos al usuario spring
RUN mkdir -p ${STORAGE_LOCATION_PATH} && \
    chown -R ${SPRING_UID}:${SPRING_GID} ${STORAGE_LOCATION_PATH}
USER spring:spring

# Crear el directorio donde se montará el volumen
RUN mkdir -p ${STORAGE_LOCATION_PATH} && chown spring:spring ${STORAGE_LOCATION_PATH}


# Set user for run app.git c
USER spring 
# Command to run the Spring Boot application with the specified profile
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${BUILD_PROFILE} -jar /app/app.jar"]

