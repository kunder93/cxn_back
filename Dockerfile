FROM eclipse-temurin:17.0.5_8-jdk-alpine
COPY app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]