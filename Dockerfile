# --- Etapa 1: Compilar el código (Build) ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiamos archivos de dependencias y código fuente
COPY pom.xml .
COPY src ./src

# Compilamos el JAR dentro de Railway
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecutar la App (Run) ---
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copiamos el JAR que acabamos de fabricar en la Etapa 1
COPY --from=build /app/target/*.jar app.jar

# Railway asigna el puerto en la variable PORT automáticamente.
# Spring Boot lo leerá si tienes server.port=${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
