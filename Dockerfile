# Etapa 1: Construcción de la aplicación
FROM openjdk:19-jdk AS build
WORKDIR /app

# Copiamos solo lo necesario para que Maven cachee dependencias
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copiamos el código fuente y compilamos
COPY src src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final con el JAR listo para correr
FROM openjdk:19-jdk
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