# Etapa 1: Construcción de la aplicación
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml primero para cachear dependencias
COPY pom.xml .

# Descargamos dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src src

# Compilamos la aplicación
RUN mvn clean package -DskipTests -B

# Etapa 2: Imagen final con el JAR listo para correr
FROM eclipse-temurin:17-jdk
WORKDIR /app
VOLUME /tmp

# Copiamos el JAR generado en la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno
ENV URL_FACHADA_FUENTE=https://tp-anual-dds-fuentes.onrender.com/

# Exponemos el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java","-jar","/app/app.jar"]