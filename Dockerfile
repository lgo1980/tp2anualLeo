# Etapa 1: Construcción de la aplicación
FROM openjdk:19-jdk AS build
WORKDIR /app

# Copiamos los archivos necesarios para la construcción
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

# Damos permisos de ejecución al wrapper de Maven y compilamos la app
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final con el JAR listo para correr
FROM openjdk:19-jdk
VOLUME /tmp

# Copiamos el JAR generado en la etapa de build
COPY --from=build /app/target/*.jar app.jar

ENV URL_FACHADA_FUENTE=https://two025-tp-entrega-2-jagrivero.onrender.com/
# Definimos el comando de inicio
ENTRYPOINT ["java","-jar","/app.jar"]

# Exponemos el puerto por defecto de Spring Boot
EXPOSE 8080