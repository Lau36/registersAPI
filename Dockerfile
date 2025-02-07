# Usar una imagen base con Java 17
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR de la aplicación al contenedor
COPY build/libs/registers_api-0.0.1-SNAPSHOT.jar /app/registers_api.jar

# Exponer el puerto en el que la aplicación Spring Boot escuchará
EXPOSE 9090

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/registers_api.jar"]