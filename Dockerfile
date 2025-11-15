# Etapa 1: Build de la aplicación
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos solo el pom para permitir caching de dependencias
COPY pom.xml .

# Descargamos dependencias (más seguro que go-offline)
RUN mvn -q dependency:resolve dependency:resolve-plugins

# Luego copiamos el código fuente
COPY src ./src

# Compilamos
RUN mvn clean package -DskipTests -q

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el jar desde el build
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno
ENV URL_FACHADA_FUENTE=https://tp-anual-dds-fuentes.onrender.com/

# Puerto de la app
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java","-jar","app.jar"]