# Utiliza una imagen base de Maven para compilar la aplicación
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Copia el código fuente de la aplicación
COPY . /usr/src/app

# Establece el directorio de trabajo
WORKDIR /usr/src/app

# Compila la aplicación
RUN mvn clean install -Pprod

# Utiliza una imagen base de Java para ejecutar la aplicación
FROM eclipse-temurin:21

# Copia el archivo JAR generado por Maven
COPY --from=builder /usr/src/app/target/back-app-1.0.1-SNAPSHOT.jar /app/app.jar

# Expone el puerto 8080
EXPOSE 8080
EXPOSE 443

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

# Comando de inicio para ejecutar la aplicación Spring Boot
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]
